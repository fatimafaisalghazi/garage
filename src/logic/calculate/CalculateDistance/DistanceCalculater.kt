package logic.calculate.CalculateDistance

import logic.Repository.DistanceRepository


class DistanceCalculator( repo: DistanceRepository) : CalculateDistance {
    private val distanceRepo = repo.getAllGovernorateInfo()

   override fun GetDistance(districtName: String?, governonate: String): Int? {

        return distanceRepo.find { it.Districtval.equals(districtName, ignoreCase = true) }?.DIstanceFromBasra
    }
}

