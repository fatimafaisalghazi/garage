package logic.calculate

class PriceCalculator(
    private val distanceCalculator: DistanceCalculator
) {
    fun calculatePrice(destinationName: String): Int {
        val distance = distanceCalculator.GetDistance(destinationName)
            ?: throw IllegalArgumentException("Distance for $destinationName not found.")

        return INITIAL_PRICE + (distance * PRICE_PER_KM)
    }

    companion object {
        private const val PRICE_PER_KM = 1000  // price per kilometer
        private const val INITIAL_PRICE = 5000 // fixed base price
    }
}
