//package presentation.gui.UI
//
//import javafx.scene.control.Alert
//import javafx.scene.control.ComboBox
//import javafx.scene.control.Label
//import javafx.scene.layout.VBox
//import javafx.scene.control.Button
//import logic.controller.PassengerRegistrationController
//import logic.entity.Passenger
//import data.DriverData.Driver
//import javafx.collections.FXCollections
//
//class DriverSelectionView(
//    private val controller: PassengerRegistrationController,
//    private val passenger: Passenger
//) : VBox(10.0) {

//    init {
//        val driverComboBox = ComboBox<Driver>()
//        driverComboBox.items.addAll(controller.(passenger.governorate))
//
//        val priceLabel = Label("Price: Calculating...")
//        val calculatePriceButton = Button("Calculate Price")
//
//        calculatePriceButton.setOnAction {
//            val selectedDriver = driverComboBox.value
//            if (selectedDriver != null) {
//                val districts = controller.getDistrictSuggestion(passenger.governorate)
//                val districtComboBox = ComboBox<String>(FXCollections.observableArrayList(districts))
//                districtComboBox.selectionModel.selectFirst()
//
//                val selectedDistrict = districtComboBox.selectionModel.selectedItem
//                val price = controller.bookSelectedDriver(passenger, selectedDriver, selectedDistrict)
//                priceLabel.text = "Price: $price"
//            } else {
//                Alert(Alert.AlertType.ERROR, "Please select a driver.").showAndWait()
//            }
//        }
//
//        val confirmButton = Button("Confirm Booking")
//        confirmButton.setOnAction {
//            val selectedDriver = driverComboBox.value
//            if (selectedDriver != null) {
//                val result = controller.bookSelectedDriver(passenger, selectedDriver, "DistrictName")
//                if (result.isSuccess) {
//                    Alert(Alert.AlertType.INFORMATION, "Booking confirmed!").showAndWait()
//                } else {
//                    Alert(Alert.AlertType.ERROR, "Error during booking: ${result.exceptionOrNull()}").showAndWait()
//                }
//            }
//        }
//
//        this.children.addAll(driverComboBox, calculatePriceButton, priceLabel, confirmButton)
//    }
//}
