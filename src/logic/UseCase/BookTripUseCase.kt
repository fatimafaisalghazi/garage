package logic.usecase

import data.DriverData.Driver
import logic.entity.Booking
import logic.entity.Passenger
import logic.repository.BookingRepository

class BookTripUseCase(private val bookingRepo: BookingRepository) {
    fun execute(passenger: Passenger, driver: Driver, price: Int): Booking {
        val booking = Booking(passenger, driver, price)
        bookingRepo.saveBooking(booking)
        return booking
    }
}
