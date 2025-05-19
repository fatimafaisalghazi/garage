package logic.Repository

import data.DistanceData.Distance

interface DistanceRepository {
    fun getAllGovernorateInfo():List<Distance>
    fun getDistrictsByGovernorate(governorate: String): List<String> }

