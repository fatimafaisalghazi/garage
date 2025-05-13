package data.Repository

import logic.entity.Booking
import logic.Repository.BookingRepository

class BookingRepositoryImpl : BookingRepository {
    //property
    private val bookings = mutableListOf<Booking>()//create an empty mutilate list of type Booking

    override fun saveBooking(booking: Booking) {
        bookings.add(booking)
    }

    override fun getAllBookings(): List<Booking> {
        return bookings
    }

}
