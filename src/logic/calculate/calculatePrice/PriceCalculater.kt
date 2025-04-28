package logic.calculate

import preasentation.readFromUser

class PriceCalculater(private val distanceCalculator: DistanceCalculator) {
    val name = readFromUser().userinput()
    fun CalculatePrice():Int?{
        val distance= distanceCalculator.getDistanceByDistrict(name)
          val price=pricePer_KG*(initPrice + distance)
        return price
    }
    companion object{
        const val pricePer_KG= 10000
        const val initPrice=5000

    }

}

