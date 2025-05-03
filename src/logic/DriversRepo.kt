package logic

import data.DriverData.Driver

interface DriversRepo {
    fun getAllDriverInfo():List<Driver>

}