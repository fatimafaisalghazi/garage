package logic.calculate

class PriceCalculator(
    private val distanceCalculator: DistanceCalculator
) {

    fun calculatePrice(passengerDistrict: String, driverGovernorate: String, carType: String): Int {
        val distance = distanceCalculator.GetDistance(passengerDistrict, driverGovernorate)
            ?: throw IllegalArgumentException("Distance from $passengerDistrict to $driverGovernorate not found.")

        val basePrice = INITIAL_PRICE + (distance * PRICE_PER_KM)
        val carTypeMultiplier = carTypeMultipliers[carType.lowercase()] ?: 1.0 // default to no change

        return (basePrice * carTypeMultiplier).toInt()
    }

    companion object {
        private const val PRICE_PER_KM = 1000
        private const val INITIAL_PRICE = 5000

        private val carTypeMultipliers = mapOf(
            "suv" to 1.5,
            "sedan" to 1.2,
            "truck" to 2.0,
            "minivan" to 1.3,
            "motorcycle" to 0.8
        )
    }
}

