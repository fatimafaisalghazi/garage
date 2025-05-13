package data.Repository

import data.DistanceData.Distance
import data.DistanceData.csvFilereaderForDistance
import data.DistanceData.csvParserForDistance
import logic.Repository.DistanceRepository

class DistanceRepositoryImpl(
    //Properties with Constructor Injection
    private val csvFileReader: csvFilereaderForDistance,
    private val distanceParser: csvParserForDistance
) : DistanceRepository {

    private val governorateInfo: List<Distance> = csvFileReader.readLinesFromFile().map { line ->
        distanceParser.parsOneline(line)
    }

    override fun getAllGovernorateInfo(): List<Distance> {
        return governorateInfo
    }

    fun getDistrictsByGovernorate(governorate: String): List<String> {
        return governorateInfo
            .filter { it.Governorate.equals(governorate, ignoreCase = true) }
            .map { it.Districtval }
            .distinct()
    }
}
