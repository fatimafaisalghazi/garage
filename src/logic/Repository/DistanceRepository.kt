package logic.Repository

import data.DistanceData.Distance

interface DistanceRepository {
    fun getAllGovernorateInfo():List<Distance>
}