package logic.controller

import data.DriverData.Driver
import presentation.gui.logic.Repository.DistanceRepository
import logic.Repository.DriverRepository
import logic.calculate.PriceCalculator
import logic.entity.Passenger
import logic.entity.Booking
import logic.usecase.BookTripUseCase
import logic.usecase.RegisterPassengerUseCase
import logic.search.FuzzyCorrection
import logic.search.DriverSearcher

class PassengerRegistrationController(
    private val driverRepo: DriverRepository,
    private val distanceRepo: DistanceRepository,
    private val priceCalculator: PriceCalculator,
    private val bookTripUseCase: BookTripUseCase,
    private val registerPassengerUseCase: RegisterPassengerUseCase
) {
    private val governorates = distanceRepo.getAllGovernorateInfo().map { it.Governorate }.distinct()
    private val districts = distanceRepo.getAllGovernorateInfo().map { it.Districtval }
    private val fuzzyGov = FuzzyCorrection(governorates)
    private val fuzzyDistrict = FuzzyCorrection(districts)

    fun validateAndCorrectPassengerInput(name: String, phone: String, governorateInput: String): Pair<Passenger?, String?> {
        if (name.isBlank() || phone.isBlank() || governorateInput.isBlank()) {
            return null to "All fields are required."
        }

        if (!phone.matches(Regex("^\\d{11}$"))) {
            return null to "Phone number must be exactly 11 digits."
        }

        val correctedGov = fuzzyGov.correct(governorateInput)
        val isValidGov = correctedGov in governorates

        if (!isValidGov) {
            return null to "Governorate \"$governorateInput\" is not supported for delivery."
        }

        val passenger = Passenger(name = name, phoneNumber = phone, governorate = correctedGov, district = "")
        registerPassengerUseCase.register(passenger)
        return passenger to if (correctedGov != governorateInput) "Did you mean \"$correctedGov\"?" else null
    }

    fun getDistrictSuggestion(input: String): String? {
        val corrected = fuzzyDistrict.correct(input)
        return if (corrected != input) corrected else null
    }

    fun isDistrictInGovernorate(district: String, governorate: String): Boolean {
        return distanceRepo.getAllGovernorateInfo()
            .any { it.Governorate == governorate && it.Districtval == district }
    }

    fun searchDriversByGovernorate(governorate: String): List<Driver> {
        val searcher = DriverSearcher(driverRepo.getAllDriverInfo(), fuzzyDistrict)
        return searcher.searchByGovernorate(governorate)
    }


    fun bookSelectedDriver(passenger: Passenger, driver: Driver, districtInput: String): Result<Booking> {
        if (!isDistrictInGovernorate(districtInput, passenger.governorate)) {
            return Result.failure(IllegalArgumentException("District \"$districtInput\" not found in ${passenger.governorate}."))
        }

        val price = priceCalculator.calculatePrice(
            districtInput, driver.Governonate, driver.TypeOfCar
        )

        val updatedPassenger = passenger.copy(district = districtInput)

        return try {
            val booking = bookTripUseCase.execute(updatedPassenger, driver)
            Result.success(booking)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
