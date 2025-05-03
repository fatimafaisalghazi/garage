package UseCase


import data.DriverData.Driver
import search.DriverSearcher
import search.FuzzyCorrection


class chooseRandomDriver(drivers: List<Driver>, corrector: FuzzyCorrection) : DriverSearcher(drivers, corrector) {

     fun chooseRandomDriverFromGovernorate(governorate: String): Driver {

        val filteredDrivers = searchByGovernorate(governorate)

        if (filteredDrivers.isEmpty()) {
            throw NoSuchElementException("No drivers found for governorate: $governorate")
        }

        return filteredDrivers.random()
    }


}