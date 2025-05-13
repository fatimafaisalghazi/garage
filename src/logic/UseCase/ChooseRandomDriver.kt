//package logic.UseCase
//
//
//import data.DriverData.Driver
//import logic.search.DriverSearcher
//import logic.search.logic.search.FuzzyCorrection
//
//
//class ChooseRandomDriver(drivers: List<Driver>, corrector: logic.search.FuzzyCorrection) : DriverSearcher(drivers, corrector) {
//
//     fun chooseRandomDriverFromGovernorate(governorate: String): Driver {
//
//        val filteredDrivers = searchByGovernorate(governorate)
//
//        if (filteredDrivers.isEmpty()) {
//            throw NoSuchElementException("No drivers found for governorate: $governorate")
//        }
//
//        return filteredDrivers.random()
//    }
//
//
//}