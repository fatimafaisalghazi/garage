import data.DistanceData.csvDistanceData
import data.DistanceData.csvFilereaderForDistance
import data.DistanceData.csvParserForDistance
import data.DriverData.csvFileReader
import data.DriverData.csvParser
import logic.DistanceRepo
import logic.DriversRepo
import java.io.File


fun main() {
   val fileName ="drivers.csv"
    var csvfile=File(fileName)
    val driversRepo:DriversRepo=csvDriverData(csvFileReader(csvfile), csvParser() )
    driversRepo.getAllDriverInfo().also {
        println("$it")
        println()
    }
    val secondFileName ="distance.csv"
    csvfile=File(secondFileName)
    val distanceRepo:DistanceRepo=csvDistanceData(csvFilereaderForDistance(csvfile), csvParserForDistance())
    distanceRepo.getAllGovernorateInfo().also {
        println(it)
    }
    }
