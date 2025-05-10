package presentation.gui.UI

import data.DistanceData.csvFilereaderForDistance
import data.DistanceData.csvParserForDistance
import data.DriverData.csvFileReader
import data.DriverData.csvParser
import data.PassengerData.PassengerRepositoryImpl
import data.Repository.BookingRepositoryImpl
import data.Repository.DriverRepositoryImpl
import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.stage.Stage
import logic.calculate.PriceCalculator
import logic.controller.PassengerRegistrationController
import logic.entity.Passenger
import logic.usecase.BookTripUseCase
import logic.usecase.RegisterPassengerUseCase
import presentation.gui.data.Repository.DistanceRepositoryImpl
import presentation.gui.logic.Repository.DistanceRepository
import presentation.gui.logic.calculate.CalculateDistance.DistanceCalculator
import java.io.File

// Setup service to initialize necessary repositories and controller
class SetupService {
    fun setupController(): PassengerRegistrationController {
        val driverCsvFile = File("drivers.csv")
        val distanceCsvFile = File("distances.csv")
        val csvParser = csvParser()
        val csvParserForDistance = csvParserForDistance()
        val driverCsvReader = csvFileReader(driverCsvFile)
        val distanceCsvReader = csvFilereaderForDistance(distanceCsvFile)
        val distanceCalculator = DistanceCalculator(listOf<DistanceRepository>())

        val driverRepo = DriverRepositoryImpl(driverCsvReader, csvParser)
        val distanceRepo = DistanceRepositoryImpl(distanceCsvReader, csvParserForDistance)
        val bookingRepo = BookingRepositoryImpl()
        val passengerRepo = PassengerRepositoryImpl()

        val priceCalculator = PriceCalculator(
            distanceCalculator = distanceCalculator
        )

        val registerPassengerUseCase = RegisterPassengerUseCase(passengerRepo)
        val bookTripUseCase = BookTripUseCase(bookingRepo, priceCalculator)

        return PassengerRegistrationController(
            driverRepo,
            distanceRepo,
            priceCalculator,
            bookTripUseCase,
            registerPassengerUseCase
        )
    }
}

class MainApp : Application() {

    private lateinit var controller: PassengerRegistrationController

    override fun start(primaryStage: Stage) {
        // Initialize the controller using SetupService
        val setupService = SetupService()
        controller = setupService.setupController()

        // Create and configure the main UI
        primaryStage.title = "Travel Garage"
        showWelcomeMessage(primaryStage)
        showMainMenu(primaryStage)

        primaryStage.show()
    }

    private fun showWelcomeMessage(stage: Stage) {
        val alert = Alert(Alert.AlertType.INFORMATION)
        alert.title = "Welcome to Travel Garage"
        alert.headerText = "Welcome to Basra Travel Garage"
        alert.contentText = """
            This application helps passengers book rides to different provinces.
            
            Would you like to:
            - Book a trip as a passenger
            - Or log in as a driver to view your latest bookings?
        """.trimIndent()

        alert.dialogPane.style = "-fx-font-size: 14px; -fx-font-family: 'Arial';"
        alert.showAndWait()
    }

    private fun showDriverLogin(stage: Stage) {
        val alert = Alert(Alert.AlertType.INFORMATION)
        alert.title = "Driver Login"
        alert.headerText = null
        alert.contentText = "Driver login is under development."
        alert.showAndWait()
    }

    private fun showMainMenu(stage: Stage) {
        val btnPassenger = Button("Register as Passenger")
        val btnDriver = Button("Driver Login")

        btnPassenger.setOnAction {
            showPassengerRegistration(stage)
        }
        btnDriver.setOnAction {
            showDriverLogin(stage)
        }


        val layout = VBox(10.0, btnPassenger, btnDriver).apply {
            padding = Insets(20.0)
        }

        stage.scene = Scene(layout, 300.0, 150.0)
    }

    private fun showPassengerRegistration(stage: Stage) {
        // Here you would add your logic to show passenger registration form
        val nameField = TextField()
        val phoneField = TextField()
        val governorateField = TextField()

        val submitButton = Button("Submit")
        submitButton.setOnAction {
            val (passenger, suggestion) = controller.validateAndCorrectPassengerInput(
                nameField.text,
                phoneField.text,
                governorateField.text
            )
            if (passenger != null) {
                // Proceed with passenger registration and next steps
                showDistrictSelection(stage, passenger)
            } else {
                showAlert(suggestion ?: "Unknown error")
            }
        }

        val form = VBox(10.0, nameField, phoneField, governorateField, submitButton).apply {
            padding = Insets(20.0)
        }

        stage.scene = Scene(form, 300.0, 200.0)
    }

    private fun showDistrictSelection(stage: Stage, passenger: Passenger) {
        val districtField = TextField()
        val submitButton = Button("Submit")

        submitButton.setOnAction {
            val district = districtField.text
            val driverList = controller.searchDriversByGovernorate(passenger.governorate)

            // Here you can list the drivers and allow user to select one
            if (driverList.isNotEmpty()) {
                // Assuming user selects a driver
                val selectedDriver = driverList[0]
                val bookingResult = controller.bookSelectedDriver(passenger, selectedDriver, district)

                if (bookingResult.isSuccess) {
                    showAlert("Booking Successful!")
                } else {
                    showAlert("Booking Failed: ${bookingResult.exceptionOrNull()?.message}")
                }
            } else {
                showAlert("No drivers found in this governorate.")
            }
        }

        val form = VBox(10.0, districtField, submitButton).apply {
            padding = Insets(20.0)
        }

        stage.scene = Scene(form, 300.0, 150.0)
    }

    private fun showAlert(message: String) {
        val alert = Alert(Alert.AlertType.INFORMATION)
        alert.title = "Information"
        alert.headerText = message
        alert.showAndWait()
    }
}

fun main() {
    Application.launch(MainApp::class.java)
}
