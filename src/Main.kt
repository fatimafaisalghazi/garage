package presentation.gui

import javafx.application.Application
import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.stage.Stage
import data.DriverData.csvFileReader
import data.DriverData.csvParser
import data.DistanceData.csvDistanceData
import data.DistanceData.csvFilereaderForDistance
import data.DistanceData.csvParserForDistance
import logic.calculate.DistanceCalculator
import logic.calculate.PriceCalculator
import logic.entity.Passenger
import logic.entity.Booking
import logic.usecase.RegisterPassengerUseCase
import logic.usecase.BookTripUseCase
import logic.repository.BookingRepository
import data.BookingData.CsvBookingRepository
import search.FuzzyCorrection
import search.DriverSearcher
import csvDriverData
import logic.DistanceRepo
import logic.DriversRepo
import logic.UseCase.ChooseRandomDriver
import data.DriverData.Driver



import java.io.File



class MainApp : Application() {
    private lateinit var stage: Stage

    // Repositories and Use-Cases
    private val driverRepo: DriversRepo by lazy {
        csvDriverData(
            CsvfileReader = csvFileReader(File("drivers.csv")),
            driverParser = csvParser()
        )
    }

    private val distanceRepo: DistanceRepo by lazy {
        csvDistanceData(
            csvFileReader = csvFilereaderForDistance(File("distance.csv")),
            distanceParser = csvParserForDistance()
        )
    }

    private val distanceCalc by lazy { DistanceCalculator(listOf(distanceRepo)) }
    private val priceCalc by lazy { PriceCalculator(distanceCalc) }
    private val bookingRepo: BookingRepository by lazy { CsvBookingRepository(File("bookings.csv")) }

    private val registerPassenger = RegisterPassengerUseCase()
    private val bookTrip = BookTripUseCase(bookingRepo)

    override fun start(primaryStage: Stage) {
        stage = primaryStage
        stage.title = "Travel Garage"
        showMainMenu()
        stage.show()
    }

    private fun showMainMenu() {
        val btnPassenger = Button("Register as Passenger")
        val btnDriver = Button("Register as Driver")

        btnPassenger.setOnAction { showPassengerRegistration() }
        btnDriver.setOnAction { showDriverLogin() }

        val layout = VBox(10.0, btnPassenger, btnDriver)
        layout.padding = Insets(20.0)

        stage.scene = Scene(layout, 300.0, 150.0)
    }

    private fun showPassengerRegistration() {
        val nameField = TextField().apply { promptText = "Name" }
        val phoneField = TextField().apply { promptText = "Phone" }
        val govField = TextField().apply { promptText = "Governorate" }
        val distField = TextField().apply { promptText = "District" }
        val btnSubmit = Button("Submit")
        val lblError = Label()

        btnSubmit.setOnAction {
            try {
                val passenger = registerPassenger.execute(
                    nameField.text,
                    phoneField.text,
                    govField.text,
                    distField.text
                )
                showSearchScene(passenger)
            } catch (e: Exception) {
                lblError.text = e.message
            }
        }

        val layout = VBox(8.0, nameField, phoneField, govField, distField, btnSubmit, lblError)
        layout.padding = Insets(20.0)

        stage.scene = Scene(layout, 350.0, 300.0)
    }

    private fun showSearchScene(passenger: Passenger) {
        val inputField = TextField().apply { promptText = "Enter district" }
        val btnSearch = Button("Search Drivers")
        val btnRandom = Button("Choose Random Driver")
        val listView = ListView<String>()
        val lblError = Label()

        btnSearch.setOnAction {
            try {
                val fc = FuzzyCorrection(distanceRepo.getAllGovernorateInfo().map { it.Districtval })
                val corrected = fc.correct(inputField.text)
                println("Corrected input: $corrected")
                val searcher = DriverSearcher(driverRepo.getAllDriverInfo(), fc)
                val drivers = searcher.searchByGovernorate(corrected)

                listView.items = FXCollections.observableArrayList(
                    drivers.map {
                        "${it.DriverName}, ${it.TypeOfCar}, ${it.Gender}, Age: ${it.DriveAge}, Gov: ${it.Governonate}"
                    }
                )
                lblError.text = if (drivers.isEmpty()) "No drivers found for: $corrected" else ""
            } catch (e: Exception) {
                lblError.text = e.message ?: "Unexpected error during search."
            }
        }

        btnRandom.setOnAction {
            try {
                val fc = FuzzyCorrection(distanceRepo.getAllGovernorateInfo().map { it.Districtval })
                val chooser = ChooseRandomDriver(driverRepo.getAllDriverInfo(), fc)
                val driver = chooser.chooseRandomDriverFromGovernorate(inputField.text)
                val distance = distanceCalc.GetDistance(inputField.text) ?: 0
                val price = priceCalc.calculatePrice(inputField.text)
                val booking = bookTrip.execute(passenger, driver, price)
                showBookingConfirmation(booking)
            } catch (e: Exception) {
                lblError.text = e.message ?: "Error during booking"
            }
        }

        val layout = VBox(10.0, inputField, btnSearch, listView, btnRandom, lblError)
        layout.padding = Insets(20.0)
        stage.scene = Scene(layout, 500.0, 400.0)
    }

    private fun showBookingConfirmation(booking: Booking) {
        val lbl = Label(
            "Booking confirmed!\n" +
                    "Passenger: ${booking.passenger.name}\n" +
                    "Driver: ${booking.driver.DriverName}\n" +
                    "Price: ${booking.price} IQD\nPay cash."
        )
        val btnBack = Button("Back to Menu").apply {
            setOnAction { showMainMenu() }
        }

        val layout = VBox(10.0, lbl, btnBack)
        layout.padding = Insets(20.0)

        stage.scene = Scene(layout, 400.0, 200.0)
    }

    private fun showDriverLogin() {
        val idField = TextField().apply { promptText = "Enter your Driver ID" }
        val btnLogin = Button("Login")
        val lblError = Label()

        btnLogin.setOnAction {
            val id = idField.text.toIntOrNull()
            if (id != null) {
                showDriverBookings(id)
            } else {
                lblError.text = "Invalid ID"
            }
        }

        val layout = VBox(10.0, idField, btnLogin, lblError)
        layout.padding = Insets(20.0)

        stage.scene = Scene(layout, 300.0, 180.0)
    }

    private fun showDriverBookings(driverId: Int) {
        val bookings = bookingRepo.getBookingsForDriver(driverId)
        val listView = ListView<String>()

        listView.items = FXCollections.observableArrayList(
            bookings.map {
                "Passenger: ${it.passenger.name}, Phone: ${it.passenger.phoneNumber}, District: ${it.passenger.district}, Price: ${it.price}"
            }
        )

        val btnBack = Button("Back to Menu").apply {
            setOnAction { showMainMenu() }
        }

        val layout = VBox(10.0, listView, btnBack)
        layout.padding = Insets(20.0)

        stage.scene = Scene(layout, 500.0, 400.0)
    }
}

fun main() = Application.launch(MainApp::class.java)
