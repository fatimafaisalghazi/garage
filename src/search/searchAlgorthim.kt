package search

import data.Driver

interface searchAlgorthim {
    fun searchByGovernorate(query: String): List<Driver>
    fun chooseRandomDriverFromGovernorate(governorate: String): Driver
}