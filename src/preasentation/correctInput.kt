package preasentation

import search.FuzzyCorrection

class UserInputCorrector(
    private val governorates: List<String>,
    private val districts: List<String>,
    private val driverNames: List<String>
) {
    private val governorateCorrector = FuzzyCorrection(governorates)
    private val districtCorrector = FuzzyCorrection(districts)
    private val driverNameCorrector = FuzzyCorrection(driverNames)

    fun correctGovernorate(input: String): String {
        return governorateCorrector.correct(input)
    }

    fun correctDistrict(input: String): String {
        return districtCorrector.correct(input)
    }

    fun correctDriverName(input: String): String {
        return driverNameCorrector.correct(input)
    }

}
