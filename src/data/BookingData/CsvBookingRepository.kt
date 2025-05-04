package data.BookingData

import logic.entity.Booking
import logic.repository.BookingRepository
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CsvBookingRepository(private val file: File) : BookingRepository {
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    override fun saveBooking(booking: Booking) {
        val line = "${booking.passenger.name},${booking.passenger.phoneNumber},${booking.passenger.governorate}," +
                "${booking.passenger.district},${booking.driver.DriverName},${booking.driver.TypeOfCar},${booking.price}," +
                "${formatter.format(booking.timestamp)}\n"
        file.appendText(line)
    }

    override fun getBookingsForDriver(driverId: Int): List<Booking> {
        return file.readLines()
            .filter { it.contains(driverId.toString()) }
            .mapNotNull { line ->
                val parts = line.split(",")
                if (parts.size >= 8) {
                    try {
                        val passenger = logic.entity.Passenger(parts[0], parts[1], parts[2], parts[3])
                        val driver = data.DriverData.Driver(parts[4], null, parts[2], "", parts[5], driverId)
                        val price = parts[6].toInt()
                        val date = formatter.parse(parts[7])
                        Booking(passenger, driver, price, date)
                    } catch (e: Exception) {
                        null
                    }
                } else null
            }
    }
}
