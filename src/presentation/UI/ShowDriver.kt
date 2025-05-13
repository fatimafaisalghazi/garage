package ui.view

import data.DriverData.Driver
import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.layout.*
import logic.controller.PassengerRegistrationController
import logic.entity.Passenger

class DriverSelectionView(
    private val passenger: Passenger,
    private val controller: PassengerRegistrationController
) : VBox() {

    private val driverComboBox = ComboBox<Driver>()
    private val districtComboBox = ComboBox<String>()
    private val priceLabel = Label("Price: -")
    private val bookButton = Button("Book Driver")
    private val messageLabel = Label()

    init {
        spacing = 15.0
        padding = Insets(20.0)

        val titleLabel = Label("Select a driver for ${passenger.name} from ${passenger.governorate}")
        titleLabel.style = "-fx-font-size: 16px; -fx-font-weight: bold"

        driverComboBox.promptText = "Choose Driver"
        districtComboBox.promptText = "Choose District"

        val drivers = passenger.governorate?.let { controller.searchDriversByGovernorate(it) }
        if (drivers != null) {
            driverComboBox.items.addAll(drivers)
        }

        val districts = passenger.governorate?.let { controller.getDistrictsByGovernorate(it) }
        if (districts != null) {
            districtComboBox.items.addAll(districts)
        }

        driverComboBox.setOnAction {
            updatePrice()
        }

        districtComboBox.setOnAction {
            updatePrice()
        }

        bookButton.setOnAction {
            bookSelectedDriver()
        }

        children.addAll(
            titleLabel,
            HBox(10.0, Label("Driver:"), driverComboBox),
            HBox(10.0, Label("District:"), districtComboBox),
            priceLabel,
            bookButton,
            messageLabel
        )
    }

    private fun updatePrice() {
        val selectedDriver = driverComboBox.value
        val selectedDistrict = districtComboBox.value

        if (selectedDriver != null && !selectedDistrict.isNullOrBlank()) {
            try {
                val price = controller.bookSelectedDriver(passenger, selectedDriver, selectedDistrict).getOrThrow().price
                priceLabel.text = "Price: $price IQD"
                messageLabel.text = ""
            } catch (e: Exception) {
                priceLabel.text = "Price: -"
                messageLabel.text = "Error calculating price: ${e.message}"
            }
        }
    }

    private fun bookSelectedDriver() {
        val selectedDriver = driverComboBox.value
        val selectedDistrict = districtComboBox.value

        if (selectedDriver == null || selectedDistrict.isNullOrBlank()) {
            messageLabel.text = "Please select both driver and district."
            return
        }

        val result = controller.bookSelectedDriver(passenger, selectedDriver, selectedDistrict)
        if (result.isSuccess) {
            val booking = result.getOrNull()
            messageLabel.text = "Booking successful! Booking Price: ${booking?.price} IQD"
        } else {
            messageLabel.text = "Booking failed: ${result.exceptionOrNull()?.message}"
        }
    }
}
