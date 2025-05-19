package presentation.gui

import data.DistanceData.csvFilereaderForDistance
import data.DistanceData.csvParserForDistance
import data.DriverData.csvFileReader
import data.DriverData.csvParser
import data.Repository.BookingRepositoryImpl
import data.Repository.DistanceRepositoryImpl
import data.Repository.DriverRepositoryImpl
import data.Repository.PassengerRepositoryImpl
import javafx.application.Application
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.stage.Stage
import logic.UseCase.BookTripUseCase
import logic.UseCase.CalculatePrice
import logic.UseCase.RegisterPassengerUseCase
import logic.calculate.CalculateDistance.DistanceCalculator
import logic.calculate.calculatePrice.PriceCalculator
import logic.controller.PassengerRegistrationController
import logic.entity.Passenger
import java.io.File

class MainApp : Application() {
    val driverCsvFile = File("drivers.csv")
    val distanceCsvFile = File("distance.csv")

    private lateinit var controller: PassengerRegistrationController
    val driverRepo = DriverRepositoryImpl(csvFileReader(driverCsvFile), csvParser())
    val distanceRepo = DistanceRepositoryImpl(csvFilereaderForDistance(distanceCsvFile), csvParserForDistance())
    val bookingRepo = BookingRepositoryImpl()
    val passengerRepo = PassengerRepositoryImpl()
    val distanceCalculator = DistanceCalculator(distanceRepo)
    val priceCalculator = PriceCalculator(distanceCalculator)

    val registerPassengerUseCase = RegisterPassengerUseCase(passengerRepo)
    val bookTripUseCase = BookTripUseCase(bookingRepo, priceCalculator)
    val price =CalculatePrice(priceCalculator,bookTripUseCase)

    override fun start(primaryStage: Stage) {

        controller = PassengerRegistrationController(
            driverRepo = driverRepo,
            distanceRepo = distanceRepo,
            registerPassengerUseCase = registerPassengerUseCase,
        )
        showWelcomeScreen(primaryStage)
    }

    private fun showWelcomeScreen(primaryStage: Stage) {
        val titleLabel = Label("Welcome to Basra Travel Garage").apply {
            font = Font.font("Arial", 22.0)
            style = "-fx-text-fill: #2e86de;"
        }

        val mainIntro = Label("This application allows you to book a ride to various governorates across Iraq.").apply {
            font = Font.font("Arial", 16.0)
            style = "-fx-text-fill: #1a5276;"
            isWrapText = true
            maxWidth = 440.0
        }

        val details = Label(
            """
            ● A car will pick you up directly from your location.
            ● We take care of your luggage.
            ● Drivers are available from both genders.
            ● A variety of car types are available based on your comfort.
            ● After booking and selecting a driver, they will contact you using your phone number.
        """.trimIndent()
        ).apply {
            font = Font.font("Arial", 14.0)
            style = "-fx-text-fill: #2c3e50;"
            isWrapText = true
            maxWidth = 440.0
        }

        val governoratesNote =
            Label("Note: Currently, we provide trips to the following governorates: Salahaddin ,Anbar ,Muthanna ,Qadisiyah ,Babel ,Wasit ,Maysan ,Karbala ,Najaf ,Baghdad .").apply {
                font = Font.font("Arial", 13.0)
                style = "-fx-text-fill: #7d6608;"
                isWrapText = true
                maxWidth = 440.0
            }

        val bookButton = Button("Book as Passenger").apply {
            style = "-fx-background-color: #27ae60; -fx-text-fill: white;"
            setOnAction { showPassengerRegistrationUI(primaryStage) }
        }
// تم إلغاء هذه الفقرة ,
// لمن اغير مصدر البيانات واعدل ع المعلومات اللي يحتاجها التطبيق أضيفها
// + ما موجودة ضمن متطلبات المشروع انما كانت فكرة اضافية هي وربط الادخالات بخوارزمية تصحيح وو

//        val driverButton = Button("Login as Driver").apply {
//            style = "-fx-background-color: #f39c12; -fx-text-fill: white;"
//            setOnAction { showDriverLogin(primaryStage) }
//        }

        val layout = VBox(18.0, titleLabel, mainIntro, details, governoratesNote, bookButton).apply {
            padding = Insets(25.0)
            alignment = Pos.CENTER
        }

        primaryStage.scene = Scene(layout, 450.0, 550.0)
        primaryStage.title = "Welcome"
        primaryStage.show()
    }
//
//    private fun showDriverLogin(stage: Stage) {
//        val alert = Alert(Alert.AlertType.INFORMATION)
//        alert.title = "Driver Login"
//        alert.headerText = null
//        alert.contentText = "Driver login is under development."
//        alert.showAndWait()
//    }


    /**
     اضافة الازرار [backToMenu] [back] [Exit]
     لتحسين تجربة المستخدم
     */
    private fun showPassengerRegistrationUI(stage: Stage) {
        val nameField = TextField().apply { promptText = "Enter your name"; prefWidth = 400.0 }
        val phoneField = TextField().apply { promptText = "Enter your phone number"; prefWidth = 400.0 }
        val governorateField = TextField().apply { promptText = "Enter your governorate"; prefWidth = 400.0 }
        val districtCombo = ComboBox<String>().apply { promptText = "Select district"; prefWidth = 400.0 }

        val nextButton = Button("Next").apply {
            style = "-fx-background-color: #2980b9; -fx-text-fill: white;"
            prefWidth = 200.0
        }

        val backButton = Button("Back").apply {
            style = "-fx-background-color: #7f8c8d; -fx-text-fill: white;"
            prefWidth = 150.0
            setOnAction { showWelcomeScreen(stage) }
        }

        val backToMenuButton = Button("Back to Menu").apply {
            style = "-fx-background-color: #e67e22; -fx-text-fill: white;"
            prefWidth = 150.0
            setOnAction { showWelcomeScreen(stage) }
        }

        val buttonBox = HBox(10.0, nextButton, backButton, backToMenuButton).apply {
            alignment = Pos.CENTER_LEFT
        }

        val exitButton = Button("Exit").apply {
            style = "-fx-background-color: #e74c3c; -fx-text-fill: white;"
            prefWidth = 200.0
            setOnAction { stage.close() }
        }



        governorateField.setOnKeyReleased {
            val inputGov = governorateField.text.trim()
            val correctedGovernorate = controller.getCorrectedGovernorate(inputGov)
            if (!correctedGovernorate.isNullOrBlank()) {
                val availableDistricts = distanceRepo.getDistrictsByGovernorate(correctedGovernorate)
                districtCombo.items.setAll(availableDistricts)
            } else {
                districtCombo.items.clear()
            }
        }

        // ✅ تحديد واحد فقط من setOnAction للـ Next
        nextButton.setOnAction {
            val name = nameField.text.trim()
            val phone = phoneField.text.trim()
            val governorate = governorateField.text.trim()
            val district = districtCombo.selectionModel.selectedItem

            val (passenger, error) = controller.validateAndCorrectPassengerInput(name, phone, governorate)
            if (passenger == null) {
                showAlert("Validation Error", error ?: "Unknown error.")
                return@setOnAction
            }
            if (district == null) {
                showAlert("Missing District", "Please select a district.")
                return@setOnAction
            }
            passenger.district = district
            passenger.governorate?.let { correctedGovernorate ->
                showDriverSelectionUI(stage, passenger, correctedGovernorate)
            }
        }

        val layout = VBox(
            12.0,
            Label("Passenger Registration").apply {
                font = Font.font("Arial", 20.0); style = "-fx-text-fill: #34495e;"
            },
            nameField, phoneField, governorateField,
            Label("Select District:"), districtCombo,
            buttonBox,
            exitButton
        ).apply {
            padding = Insets(25.0)
            alignment = Pos.TOP_CENTER
        }


        stage.scene = Scene(layout, 450.0, 600.0)
        stage.title = "Passenger Booking"
        stage.show()
    }

    private fun showDriverSelectionUI(stage: Stage, passenger: Passenger, governorate: String) {
        val drivers = controller.getDriversByGovernorate(governorate)
        if (drivers.isEmpty()) {
            showAlert("No Drivers", "No drivers found in $governorate.")
            return
        }

        val driversCombo = ComboBox<String>().apply {
            items.setAll(drivers.map { "${it.DriverName} | ${it.TypeOfCar} | Age: ${it.DriveAge}" })
            prefWidth = 400.0
        }

        val bookButton = Button("Book Ride").apply {
            style = "-fx-background-color: #27ae60; -fx-text-fill: white;"
        }

        val backButton = Button("Back").apply {
            style = "-fx-background-color: #7f8c8d; -fx-text-fill: white;"
            prefWidth = 150.0
            setOnAction { showPassengerRegistrationUI(stage) }
        }

        val backToMenuButton = Button("Back to Menu").apply {
            style = "-fx-background-color: #e67e22; -fx-text-fill: white;"
            prefWidth = 150.0
            setOnAction { showWelcomeScreen(stage) }
        }

        val exitButton = Button("Exit").apply {
            style = "-fx-background-color: #e74c3c; -fx-text-fill: white;"
            setOnAction { stage.close() }
        }

        bookButton.setOnAction {
            val selectedIndex = driversCombo.selectionModel.selectedIndex
            if (selectedIndex == -1) {
                showAlert("Error", "Please select a driver.")
                return@setOnAction
            }
            val selectedDriver = drivers[selectedIndex]

            val passengerDistrict = passenger.district
            val driverGovernorate = selectedDriver.Governonate
            val carType = selectedDriver.TypeOfCar

            if (!passengerDistrict.isNullOrBlank() && !driverGovernorate.isNullOrBlank() && !carType.isNullOrBlank()) {
                try {
                    val price =price.calculateTripPrice(
                        passengerDistrict = passengerDistrict,
                        driverGovernorate = driverGovernorate,
                        carType = carType
                    )
                    showAlert(
                        "Booking Confirmed",
                        "Booking successful with driver: ${selectedDriver.DriverName}\nTotal price: $price Thousand IQD \n payment will be directly to the driver <<< pay by cash"
                    )
                } catch (e: IllegalArgumentException) {
                    showAlert("Distance Error", e.message ?: "Unable to calculate trip distance.")
                }
            } else {
                showAlert(
                    "Missing Data",
                    "Please make sure district, governorate, and car type are all selected correctly."
                )
            }
        }

        val layout = VBox(
            15.0,
            Label("Select a Driver").apply {
                font = Font.font("Arial", 18.0)
                style = "-fx-text-fill: #2c3e50;"
            },
            driversCombo,
            HBox(10.0, bookButton, backButton, backToMenuButton, exitButton).apply {
                alignment = Pos.CENTER_LEFT
            }
        ).apply {
            padding = Insets(20.0)
            alignment = Pos.TOP_CENTER
        }

        stage.scene = Scene(layout, 450.0, 300.0)
        stage.title = "Choose Driver"
        stage.show()
    }}
    private fun showAlert(title: String, message: String) {
        val alert = Alert(Alert.AlertType.INFORMATION)
        alert.title = title
        alert.headerText = null
        alert.contentText = message
        alert.showAndWait()
    }

    fun main() = Application.launch(MainApp::class.java)
