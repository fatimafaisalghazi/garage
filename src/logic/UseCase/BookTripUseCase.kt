package logic.UseCase

import data.DriverData.Driver
import logic.calculate.calculatePrice.PriceCalculator
import logic.entity.Booking
import logic.entity.Passenger
import logic.Repository.BookingRepository

class BookTripUseCase(
    private val bookingRepo: BookingRepository,
    private val priceCalculator: PriceCalculator
) {
    fun execute(passenger: Passenger, driver: Driver): Booking {
        val price = priceCalculator.calculatePrice(
            passengerDistrict = passenger.district,
            driverGovernorate = driver.Governonate,
            carType = driver.TypeOfCar
        )

        val booking = Booking(passenger, driver, price)
        try {
            bookingRepo.saveBooking(booking)
        } catch (e: Exception) {
            throw IllegalStateException(" failed in Booking : ${e.message}", e)
        }
        return booking
    }
}
