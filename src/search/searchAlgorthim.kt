package search

import data.DriverData.Driver

interface searchAlgorthim {
    fun searchByGovernorate(query: String): List<Driver>
}