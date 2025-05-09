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
import logic.usecase.RegisterPassengerUseCase
import logic.usecase.BookTripUseCase
import data.BookingData.CsvBookingRepository
import search.FuzzyCorrection
import search.DriverSearcher
import csvDriverData
import data.DriverData.Driver
import logic.DistanceRepo
import logic.DriversRepo
import logic.entity.Booking
import java.io.File

class MainApp : Application() {
    private lateinit var stage: Stage

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
    private val bookingRepo = CsvBookingRepository(File("bookings.csv"))
    private val registerPassenger = RegisterPassengerUseCase()
    private val bookTrip = BookTripUseCase(bookingRepo, priceCalculator = priceCalc)

    override fun start(primaryStage: Stage) {
        stage = primaryStage
        stage.title = "Travel Garage"
        showWelcomeMessage()
        showMainMenu()
        stage.show()
    }

    private fun showWelcomeMessage() {
        val alert = Alert(Alert.AlertType.INFORMATION)
        alert.title = "Welcome to Travel Garage"
        alert.headerText = "Welcome to Basra Travel Garage"
        alert.contentText = """
            This application helps passengers book rides to different provinces.
            
            Would you like to:
            - Book a trip as a passenger
            - Or log in as a driver to view your latest bookings?

            Please use the next menu to choose.
        """.trimIndent()

        alert.dialogPane.style = "-fx-font-size: 14px; -fx-font-family: 'Arial';"
        alert.showAndWait()
    }

    private fun showMainMenu() {
        val btnPassenger = Button("Register as Passenger")
        val btnDriver = Button("Driver Login")

        btnPassenger.setOnAction { showPassengerRegistration() }
        btnDriver.setOnAction { showDriverLogin() }

        val layout = VBox(10.0, btnPassenger, btnDriver).apply {
            padding = Insets(20.0)
        }

        stage.scene = Scene(layout, 300.0, 150.0)
    }

    private fun showPassengerRegistration() {
        val nameField = TextField().apply { promptText = "Name" }
        val phoneField = TextField().apply { promptText = "Phone (11 digits)" }
        val govField = TextField().apply { promptText = "Governorate" }
        val btnSubmit = Button("Next")
        val lblError = Label()

        btnSubmit.setOnAction {
            fun showNextStep(name: String, phone: String, correctedGov: String) {
                val passenger = Passenger(name = name, phoneNumber = phone, governorate = correctedGov, district = "")
                showSearchScene(passenger) // الانتقال للخطوة التالية
            }
            try {
                val phone = phoneField.text
                val name = nameField.text
                val gov = govField.text

                if (!phone.matches(Regex("^\\d{11}$"))) {
                    lblError.text = "Phone number must be exactly 11 digits with no symbols or letters."
                    return@setOnAction
                }

                val governorates = distanceRepo.getAllGovernorateInfo().map { it.Governorate }.distinct()
                val fc = FuzzyCorrection(governorates)
                val correctedGov = fc.correct(gov)

                if (correctedGov != gov) {
                    lblError.text = "Did you mean \"$correctedGov\"?"
                    // عرض زر "نعم هذا صحيح"
                    val btnConfirm = Button("Yes, that's correct")
                    btnConfirm.setOnAction {
                        govField.text = correctedGov // تحديث الحقل بالمحافظة المصححة
                        showNextStep(name, phone, correctedGov)
                    }
                    val layout = VBox(10.0, btnConfirm) // عرض زر التأكيد
                    stage.scene = Scene(layout, 300.0, 100.0)
                    return@setOnAction
                }

                if (correctedGov !in governorates) {
                    lblError.text = "Governorate \"$correctedGov\" not found in our data. We don't deliver there."
                    return@setOnAction
                }

                showNextStep(name, phone, correctedGov)

            } catch (e: Exception) {
                lblError.text = e.message ?: "An unexpected error occurred during registration."
            }
        }




    }
    private fun showSearchScene(passenger: Passenger) {
        val inputField = TextField().apply { promptText = "Enter district" }
        val btnSearch = Button("Search Drivers")
        val btnBook = Button("Book Selected Driver")
        val listView = ListView<String>()
        val lblError = Label()

        var matchedDrivers: List<Driver> = emptyList()

        val fc = FuzzyCorrection(distanceRepo.getAllGovernorateInfo().map { it.Districtval })
        val corrected = fc.correct(inputField.text)

        lblError.text = if (corrected != inputField.text) {
            "Did you mean: \"$corrected\"?"
        } else {
            ""
        }

// ✅ فقط نبحث داخل المحافظة التي سجلها الراكب
        val districtsInGovernorate = distanceRepo
            .getAllGovernorateInfo()
            .filter { it.Governorate == passenger.governorate }
            .map { it.Districtval }

        if (!districtsInGovernorate.contains(corrected)) {
            lblError.text = "District \"$corrected\" not found in ${passenger.governorate}."
            listView.items.clear()
            return
        }

// ✅ نبحث عن السواق الذين في نفس المحافظة
        val searcher = DriverSearcher(driverRepo.getAllDriverInfo(), fc)
        matchedDrivers = searcher.searchByGovernorate(passenger.governorate)

        listView.items = FXCollections.observableArrayList(
            matchedDrivers.mapIndexed { index, driver ->
                "$index. ${driver.DriverName}, ${driver.TypeOfCar}, ${driver.Gender}, Age: ${driver.DriveAge}, Gov: ${driver.Governonate}"
            }
        )

        if (matchedDrivers.isEmpty()) {
            lblError.text = "No drivers found in governorate: ${passenger.governorate}"
        }




        btnBook.setOnAction {
            val selectedIndex = listView.selectionModel.selectedIndex
            if (selectedIndex >= 0 && selectedIndex < matchedDrivers.size) {
                val selectedDriver = matchedDrivers[selectedIndex]
                try {
                    val price = priceCalc.calculatePrice(
                        passenger.district, selectedDriver.Governonate,
                        carType = selectedDriver.TypeOfCar
                    )
                    val booking = bookTrip.execute(
                        passenger = passenger,
                        driver = selectedDriver
                    )
                    showBookingConfirmation(booking)
                } catch (e: IllegalArgumentException) {
                    lblError.text = e.message ?: "We do not deliver to the selected governorate."
                } catch (e: Exception) {
                    lblError.text = e.message ?: "Error during booking."
                }
            } else {
                lblError.text = "Please select a driver to book."
            }
        }

        val layout = VBox(10.0, inputField, btnSearch, listView, btnBook, lblError)
        layout.padding = Insets(20.0)

        stage.scene = Scene(layout, 600.0, 400.0)
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

        val layout = VBox(10.0, lbl, btnBack).apply {
            padding = Insets(20.0)
        }

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

        val layout = VBox(10.0, idField, btnLogin, lblError).apply {
            padding = Insets(20.0)
        }

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

        val layout = VBox(10.0, listView, btnBack).apply {
            padding = Insets(20.0)
        }

        stage.scene = Scene(layout, 500.0, 400.0)
    }
}

fun main() = Application.launch(MainApp::class.java)
