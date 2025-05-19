package logic.Repository

import logic.entity.Passenger

interface PassengerRepository {
    fun save(passenger: Passenger)
    fun getAll(): List<Passenger>
}
