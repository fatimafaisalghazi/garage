package logic.UseCase

import logic.Repository.PassengerRepository
import logic.entity.Passenger

class RegisterPassengerUseCase(private val passengerRepository: PassengerRepository) {
    fun register(passenger: Passenger) {
        passengerRepository.save(passenger)
    }
}
