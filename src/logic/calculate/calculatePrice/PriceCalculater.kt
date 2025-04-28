package logic.calculate

import preasentation.readFromUser

class PriceCalculater(private val distanceCalculator: DistanceCalculator) {
    val name = readFromUser().userinput()
    fun CalculatePrice():Int?{
        val distance= distanceCalculator.getDistanceByDistrict(name)
          val price=PRICE_PER_KG*(INITIAL_PRICE + distance!!)
        return price
    }
    companion object{
        const val PRICE_PER_KG= 10000
        const val INITIAL_PRICE=5000

    }

}

