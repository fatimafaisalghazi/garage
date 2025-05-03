package logic

import data.DistanceData.Distance

interface DistanceRepo {
    fun getAllGovernorateInfo():List<Distance>
}