package logic.search

import data.DriverData.Driver

interface searchAlgorthim {
    fun searchByGovernorate(Governonate: String): List<Driver>
}