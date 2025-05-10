package data.BookingData

import data.DriverData.Driver
import logic.entity.Booking
import logic.entity.Passenger
import logic.repository.BookingRepository
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CsvBookingRepository(private val file: File) : BookingRepository {
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    override fun saveBooking(booking: Booking) {
        val line = listOf(
            booking.passenger.name,
            booking.passenger.phoneNumber,
            booking.passenger.governorate,
            booking.passenger.district,
            booking.driver.DriverName,
            booking.driver.TypeOfCar,
            booking.driver.Gender, // added gender
            booking.driver.DriveID,
            booking.driver.DriveAge,
            booking.price,
            formatter.format(booking.timestamp)
        ).joinToString(",") + "\n"

        file.appendText(line)
    }

    override fun getBookingsForDriver(driverId: Int): List<Booking> {
        return file.readLines()
            .mapNotNull { line ->
                val parts = line.split(",")
                if (parts.size >= 10) {
                    try {
                        val passenger = Passenger(parts[0], parts[1], parts[2], parts[3])
                        val driver = Driver(
                            DriverName = parts[4],
                            Governonate = parts[2],
                            Gender = parts[6],
                            TypeOfCar = parts[5],
                            DriveID = parts[7].toInt(),
                            DriveAge =parts [8].toInt()
                        )
                        val price = parts[9].toInt()
                        val date = formatter.parse(parts[10]) ?: Date()
                        if (driver.DriveID == driverId) {
                            Booking(passenger, driver, price, date)
                        } else null
                    } catch (e: Exception) {
                        null
                    }
                } else null
            }
    }
}
