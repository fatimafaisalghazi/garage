package logic.calculate.CalculateDistance

import logic.Repository.DistanceRepository


// هذا الكلاس هو المسؤول عن حساب المسافات
class DistanceCalculator( repo: DistanceRepository) : CalculateDistance {
    private val distanceRepo = repo.getAllGovernorateInfo()


   override fun GetDistance(districtName: String?, governonate: String): Int? {
        // بحث داخل القائمة عن المنطقة بالاسم
        return distanceRepo.find { it.Districtval.equals(districtName, ignoreCase = true) }?.DIstanceFromBasra
    }
}

