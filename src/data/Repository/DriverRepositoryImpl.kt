package data.Repository

import data.DriverData.Driver
import data.DriverData.csvFileReader
import data.DriverData.csvParser
import logic.Repository.DriverRepository

class DriverRepositoryImpl(
    private val csvFileReader: csvFileReader,
    private val driverParser: csvParser
) : DriverRepository {

    override fun getAllDrivers(): List<Driver> {
        val allDriverInfo: MutableList<Driver> = mutableListOf()

        csvFileReader.readLinesFromFile().forEach { lineOfCsv ->
            val parsDriver: Driver = driverParser.parsOneLine(lineOfCsv)
            allDriverInfo.add(parsDriver)
        }
        return allDriverInfo // معلومات السايق كلها المأخوذة من الفايل
    }

    override fun getDriverById(id: Int): Driver? {
        return getAllDrivers().find { it.DriveID == id }
    }

}



