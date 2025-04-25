package search
import data.DriverData.Driver

class DriverSearcher(
    private val drivers: List<Driver>,
    private val corrector: FuzzyCorrection
    ): searchAlgorthim{
       override fun searchByGovernorate(query: String): List<Driver> {
            val corrected = corrector.correct(query)
            return drivers.filter { it.Governonate.equals(corrected, ignoreCase = true) }
        }
    }

