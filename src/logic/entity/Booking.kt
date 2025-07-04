package logic.entity

import data.DriverData.Driver
import java.util.Date

data class Booking(
    val passenger: Passenger,
    val driver: Driver,
    val price: Int,
    val timestamp: Date = Date()
)
