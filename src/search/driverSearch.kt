package search

import data.Driver

class DriverSearcher(
    private val drivers: List<Driver>,
    private val corrector: FuzzyCorrection
) : searchAlgorthim {

    override fun searchByGovernorate(query: String): List<Driver> {
        val corrected = corrector.correct(query)
        return drivers.filter { it.Governonate.equals(corrected, ignoreCase = true) }
    }

   override fun chooseRandomDriverFromGovernorate(governorate: String): Driver {
        val filteredDrivers = searchByGovernorate(governorate)

        if (filteredDrivers.isEmpty()) {
            throw NoSuchElementException("No drivers found for governorate: $governorate")
        }

        return filteredDrivers.random()
    }
}
