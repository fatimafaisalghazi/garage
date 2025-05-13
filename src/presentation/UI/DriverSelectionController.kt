package ui

import data.DriverData.Driver
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.*
import logic.controller.PassengerRegistrationController
import logic.Repository.DriverRepository
import logic.Repository.DistanceRepository
import logic.calculate.calculatePrice.PriceCalculator
import logic.UseCase.BookTripUseCase
import logic.UseCase.RegisterPassengerUseCase
import logic.entity.Passenger

class DriverSelectionController(private val passenger: Passenger) {

    @FXML private lateinit var driverComboBox: ComboBox<Driver>
    @FXML private lateinit var districtField: TextField
    @FXML private lateinit var bookButton: Button
    @FXML private lateinit var resultLabel: Label
    private lateinit var driverRepo:DriverRepository
    private lateinit var distanceRepo:DistanceRepository
    private lateinit var priceCalculator:PriceCalculator
    private lateinit var bookTripUseCase:BookTripUseCase
    private lateinit var registerPassengerUseCase:RegisterPassengerUseCase




    private val controller = PassengerRegistrationController(
        driverRepo = driverRepo,
        distanceRepo = distanceRepo,
        priceCalculator =priceCalculator ,
        bookTripUseCase = bookTripUseCase,
        registerPassengerUseCase = registerPassengerUseCase
    )

    @FXML
    fun initialize() {
        val drivers = passenger.governorate?.let { controller.searchDriversByGovernorate(it) }
        driverComboBox.items = FXCollections.observableArrayList(drivers)

        bookButton.setOnAction {
            handleBooking()
        }
    }

    private fun handleBooking() {
        val selectedDriver = driverComboBox.value
        val districtInput = districtField.text

        if (selectedDriver == null || districtInput.isBlank()) {
            resultLabel.text = "Please select a driver and enter a district."
            return
        }

        val result = controller.bookSelectedDriver(passenger, selectedDriver, districtInput)

        if (result.isSuccess) {
            val booking = result.getOrNull()
            resultLabel.text = "Booking confirmed! Price: ${booking?.price}"
        } else {
            resultLabel.text = result.exceptionOrNull()?.message ?: "Booking failed."
        }
    }
}
