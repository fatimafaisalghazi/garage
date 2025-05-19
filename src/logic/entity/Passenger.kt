package logic.entity

data class Passenger(
    val name: String,
    val phoneNumber: String,
    val governorate: String?,
    var district: String
)
