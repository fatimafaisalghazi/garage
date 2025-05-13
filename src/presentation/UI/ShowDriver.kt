package presentation.UI

import data.DriverData.Driver
import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.stage.Stage
import logic.calculate.calculatePrice.PriceCalculator

class ChooseDriverView(
    private val drivers: List<Driver>,
    private val passengerGovernorate: String,
    private val passengerDistrict: String
) {

     private lateinit var  priceCalculator : PriceCalculator

    fun show() {
        val stage = Stage()
        stage.title = "Choose a Driver"

        val comboBox = ComboBox<Driver>()
        comboBox.items = FXCollections.observableArrayList(drivers)
        comboBox.promptText = "Select a driver"

        val priceLabel = Label("Price: ")
        val confirmButton = Button("Confirm Booking")
        confirmButton.isDisable = true

        comboBox.setOnAction {
            val selectedDriver = comboBox.value
            if (selectedDriver != null) {
                val price = priceCalculator.calculatePrice(
                    passengerDistrict = passengerDistrict,
                    driverGovernorate = selectedDriver.Governonate,
                    carType = selectedDriver.TypeOfCar
                )
                priceLabel.text = "Price: \$${price}"
                confirmButton.isDisable = false
            }
        }

        confirmButton.setOnAction {
            val selectedDriver = comboBox.value
            if (selectedDriver != null) {
                Alert(Alert.AlertType.INFORMATION).apply {
                    title = "Booking Confirmed"
                    headerText = null
                    contentText = "Booking confirmed with ${selectedDriver.DriverName}."
                    showAndWait()
                }
                stage.close()
            }
        }

        val vbox = VBox(10.0, comboBox, priceLabel, confirmButton)
        vbox.padding = Insets(20.0)

        stage.scene = Scene(vbox, 400.0, 200.0)
        stage.show()
    }
}
