package logic.calculate

import data.DistanceData.*
import logic.DistanceRepo
import java.io.File



// هذا الكلاس هو المسؤول عن حساب المسافات
class DistanceCalculator(private val districts: List<DistanceRepo>):CalculateDistance {
    // هنا نستخدم getAllGovernorateInfo
    val distancerepo = districts.flatMap { it.getAllGovernorateInfo() }

   override fun GetDistance(districtName: String?): Int? {
        // بحث داخل القائمة عن المنطقة بالاسم
        return distancerepo.find { it.Districtval.equals(districtName, ignoreCase = true) }?.DIstanceFromBasra
    }


}



fun main() {
    // قراءة بيانات الملف وتخزينها في قائمة
    val distanceFile = File("distance.csv")
    val distances = distanceFile.readLines().drop(1) // لتجاهل أول سطر إذا كان يحتوي على رؤوس الأعمدة


    // إنشاء الكائنات المطلوبة
    val distanceRepo = csvDistanceData(
        csvFileReader = csvFilereaderForDistance(distanceFile),
        distanceParser = csvParserForDistance()
    )
    val distanceCalculator = DistanceCalculator(listOf(distanceRepo))

    // طلب إدخال من المستخدم
    println("Enter a district name: ")
    val name = readLine()

    // حساب المسافة وعرض النتيجة
    val distance = distanceCalculator.GetDistance(name)
    if (distance != null) {
        println("The distance from Basra to $name is $distance km.")
    } else {
        println("District not found.")
    }
}
