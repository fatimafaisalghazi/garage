package logic.search

import data.DriverData.Driver

interface searchAlgorthim {
    fun searchByGovernorate(query: String): List<Driver>
}