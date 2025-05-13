package data.Repository

import data.DriverData.Driver
import data.DriverData.csvFileReader
import data.DriverData.csvParser
import logic.Repository.DriverRepository

class DriverRepositoryImpl(
    private val CsvfileReader: csvFileReader,
    private val driverParser: csvParser
) : DriverRepository {

    override fun getAllDrivers(): List<Driver> {
        val allDriver: MutableList<Driver> = mutableListOf()
        CsvfileReader.readLinesFromFile().forEach { lineOfCsv ->
            val parsDriver: Driver = driverParser.parsOneLine(lineOfCsv)
            allDriver.add(parsDriver)
        }
        return allDriver
    }

    override fun getDriverById(id: Int): Driver? {
        return getAllDrivers().find { it.DriveID == id }
    }


}



