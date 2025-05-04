package logic.repository

import logic.entity.Booking

interface BookingRepository {
    fun saveBooking(booking: Booking)
    fun getBookingsForDriver(driverId: Int): List<Booking>
}
