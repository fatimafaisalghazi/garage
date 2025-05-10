package presentation.gui.UI

import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.stage.Stage
import logic.controller.PassengerRegistrationController
import logic.usecase.BookTripUseCase
import logic.usecase.RegisterPassengerUseCase
import data.DriverData.csvFileReader
import data.DriverData.csvParser
import data.DistanceData.csvFilereaderForDistance
import data.DistanceData.csvParserForDistance
import data.Repository.BookingRepositoryImpl
import data.Repository.DriverRepositoryImpl
import data.PassengerData.PassengerRepositoryImpl
import logic.calculate.PriceCalculator
import presentation.gui.logic.calculate.CalculateDistance.DistanceCalculator
import presentation.gui.data.Repository.DistanceRepositoryImpl
import java.io.File
import javafx.scene.text.Font

class MainApp : Application() {
    private lateinit var controller: PassengerRegistrationController

    override fun start(primaryStage: Stage) {
        // ====== Setup Dependencies =======
        val driverCsvFile = File("drivers.csv")
        val distanceCsvFile = File("distance.csv")

        val driverRepo = DriverRepositoryImpl(csvFileReader(driverCsvFile), csvParser())
        val distanceRepo = DistanceRepositoryImpl(csvFilereaderForDistance(distanceCsvFile), csvParserForDistance())
        val bookingRepo = BookingRepositoryImpl()
        val passengerRepo = PassengerRepositoryImpl()
        val distanceCalculator = DistanceCalculator(distanceRepo)
        val priceCalculator = PriceCalculator(distanceCalculator)

        val registerPassengerUseCase = RegisterPassengerUseCase(passengerRepo)
        val bookTripUseCase = BookTripUseCase(bookingRepo, priceCalculator)

        controller = PassengerRegistrationController(
            driverRepo = driverRepo,
            distanceRepo = distanceRepo,
            priceCalculator = priceCalculator,
            bookTripUseCase = bookTripUseCase,
            registerPassengerUseCase = registerPassengerUseCase,
        )


        showPassengerRegistrationUI(primaryStage)
    }

    private fun showPassengerRegistrationUI(stage: Stage) {
        val nameField = TextField()
        val phoneField = TextField()
        val governorateField = TextField()
        val districtCombo = ComboBox<String>()
        val driversCombo = ComboBox<String>()

        nameField.promptText = "Enter your name"
        phoneField.promptText = "Enter your phone number"
        governorateField.promptText = "Enter your governorate"

        // تحديث قائمة المناطق حسب المحافظة
        governorateField.setOnKeyReleased {
            val governorate = governorateField.text.trim()
            val districts = controller.getDistrictSuggestion(governorate)
            districtCombo.items.setAll(districts)
        }

        // عند اختيار الحي، عرض السواق لنفس المحافظة
        districtCombo.setOnAction {
            val governorate = governorateField.text.trim()
            val drivers = controller.searchDriversByGovernorate(governorate)
            driversCombo.items.setAll(drivers.map { "${it.DriverName} | ${it.TypeOfCar}" })
        }

        val bookButton = Button("Book Ride")
        bookButton.setOnAction {
            val name = nameField.text.trim()
            val phone = phoneField.text.trim()
            val governorate = governorateField.text.trim()
            val district = districtCombo.selectionModel.selectedItem
            val selectedDriverIndex = driversCombo.selectionModel.selectedIndex

            val (passenger, error) = controller.validateAndCorrectPassengerInput(name, phone, governorate)
            if (passenger == null) {
                showAlert("Error", error!!)
                return@setOnAction
            }

            val drivers = controller.searchDriversByGovernorate(governorate)
            if (selectedDriverIndex == -1 || district == null) {
                showAlert("Error", "Please select a driver and a district.")
                return@setOnAction
            }

            val selectedDriver = drivers[selectedDriverIndex]
            val result = controller.bookSelectedDriver(passenger, selectedDriver, district)

            if (result.isSuccess) {
                val booking = result.getOrNull()
                showAlert("Booking Confirmed", "Trip booked with price: ${booking?.price} IQD")
            } else {
                showAlert("Booking Failed", result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }

        val layout = VBox(10.0,
            Label("Passenger Registration").apply { font = Font.font(18.0) },
            nameField,
            phoneField,
            governorateField,
            Label("Select District:"), districtCombo,
            Label("Available Drivers:"), driversCombo,
            bookButton
        )
        layout.padding = Insets(20.0)

        stage.scene = Scene(layout, 400.0, 500.0)
        stage.title = "Passenger Booking"
        stage.show()
    }

    private fun showAlert(title: String, message: String) {
        val alert = Alert(Alert.AlertType.INFORMATION)
        alert.title = title
        alert.headerText = null
        alert.contentText = message
        alert.showAndWait()
    }
}

fun main() = Application.launch(MainApp::class.java)
