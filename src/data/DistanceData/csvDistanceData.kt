package data.DistanceData


import logic.DistanceRepo
import java.io.File

class csvDistanceData(private val csvFileReader: csvFilereaderForDistance,
    private val distanceParser:csvParserForDistance
):DistanceRepo{
    override fun getAllGovernorateInfo():List<Distance>{
        val allGoveronateDistance:MutableList<Distance> = mutableListOf()
        csvFileReader.readLinesFromFile().forEach{lineOfCsv ->
            val parsDistance:Distance = distanceParser.parsOneline(lineOfCsv)
            allGoveronateDistance.add(parsDistance)
        }
        return allGoveronateDistance
    }
}