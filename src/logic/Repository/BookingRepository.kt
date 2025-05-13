package logic.Repository

import logic.entity.Booking

interface BookingRepository {
    fun saveBooking(booking: Booking)
    fun getAllBookings(): List<Booking>
}
