package logic.controller

import data.DriverData.Driver
import logic.Repository.DistanceRepository
import logic.Repository.DriverRepository
import logic.UseCase.RegisterPassengerUseCase
import logic.entity.Passenger
import logic.search.FuzzyCorrection

class PassengerRegistrationController(
    private val driverRepo: DriverRepository,
    private val distanceRepo: DistanceRepository,

    private val registerPassengerUseCase: RegisterPassengerUseCase
) {
    private val governorates = distanceRepo.getAllGovernorateInfo().map { it.Governorate }.distinct()
    private val districts = distanceRepo.getAllGovernorateInfo().map { it.Districtval }
    private val correctionOfGovernorates = FuzzyCorrection(governorates)

    fun validateAndCorrectPassengerInput(
        name: String,
        phone: String,
        governorateInput: String
    ): Pair<Passenger?, String?> {
        if (name.isBlank() || phone.isBlank() || governorateInput.isBlank()) {
            return null to "All fields are required."
        }

        if (!phone.matches(Regex("^\\d{11}$"))) {
            return null to "Phone number must be exactly 11 digits."
        }

        val correctedGov = correctionOfGovernorates.correct(governorateInput)
        val isValidGov = correctedGov in governorates

        if (!isValidGov) {
            return null to "Governorate \"$governorateInput\" is not supported for delivery."
        }

        val passenger = Passenger(name = name, phoneNumber = phone, governorate = correctedGov, district = "")
        registerPassengerUseCase.register(passenger)
        return passenger to if (correctedGov != governorateInput) "Did you mean \"$correctedGov\"?" else null
    }

    fun getDriversByGovernorate(governorate: String): List<Driver> {
        // استخدام المحافظة المصححة
        val correctedGovernorate = correctionOfGovernorates.correct(governorate)
        return driverRepo.getAllDrivers().filter {
            it.Governonate.equals(correctedGovernorate, ignoreCase = true)
        }
    }

    fun getCorrectedGovernorate(input: String): String? {
        val corrected = correctionOfGovernorates.correct(input)
        return if (corrected in governorates) corrected else null
    }
}

