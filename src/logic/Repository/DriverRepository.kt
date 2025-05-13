package logic.Repository

import data.DriverData.Driver


interface DriverRepository {
    fun getAllDrivers(): List<Driver>
    fun getDriverById(id: Int): Driver?
}
