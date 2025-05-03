import data.DriverData.Driver
import data.DriverData.csvParser
import logic.DriversRepo
import data.DriverData.csvFileReader

class csvDriverData(private val  CsvfileReader: csvFileReader,
                    private val driverParser: csvParser
):DriversRepo {

    override fun getAllDriverInfo(): List<Driver> {
        val allDriver:MutableList<Driver> = mutableListOf()
        CsvfileReader.readLinesFromFile().forEach{lineOfCsv ->
             val parsDriver : Driver =  driverParser.parsOneLine(lineOfCsv)
            allDriver.add(parsDriver)
        }
        return allDriver
    }

}

