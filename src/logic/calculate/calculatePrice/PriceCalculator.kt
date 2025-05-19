package logic.calculate.calculatePrice

import logic.calculate.CalculateDistance.DistanceCalculator
import kotlin.math.round

class PriceCalculator(
    private val distanceCalculator: DistanceCalculator
) {

    fun calculatePrice(passengerDistrict: String, driverGovernorate: String, carType: String): Int {
        val distance = distanceCalculator.GetDistance(passengerDistrict, driverGovernorate)
            ?: throw IllegalArgumentException("Distance from $passengerDistrict to $driverGovernorate not found.")

        val basePrice = INITIAL_PRICE + (distance * PRICE_PER_KM)
        val carTypeMultiplier = carTypeMultipliers[carType.lowercase()] ?: 1.0

        return round(basePrice * carTypeMultiplier).toInt()
    }

    companion object {
        private const val PRICE_PER_KM = 0.05
        private const val INITIAL_PRICE = 12

        private val carTypeMultipliers = mapOf(
            "hyundai elantra" to 1.2,
            "chevrolet spark" to 1.0,
            "nissan sunny" to 1.3,
            "toyota corolla" to 1.1,
            "kia bongo" to 0.9
        )
    }
}