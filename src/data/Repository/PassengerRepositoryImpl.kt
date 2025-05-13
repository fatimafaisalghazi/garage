package data.Repository

import logic.Repository.PassengerRepository
import logic.entity.Passenger

class PassengerRepositoryImpl : PassengerRepository {
    private val passengers = mutableListOf<Passenger>()

    override fun save(passenger: Passenger) {
        passengers.add(passenger)
    }

    override fun getAll(): List<Passenger> = passengers

    override fun findByPhone(phone: String): Passenger? {
        return passengers.find { it.phoneNumber == phone }
    }
}
