import data.Driver
import data.csvParser
import logic.DriversRepo
import data.csvFileReader

class csvData(private val  CsvfileReader:csvFileReader ,
    private val driverParser: csvParser
):DriversRepo {

    override fun getAllDriverInfo(): List<Driver> {
        val allDriver:MutableList<Driver> = mutableListOf()
        CsvfileReader.readLinesFromFile().forEach{lineOfCsv ->
             val parsDriver =  driverParser.parsOneLine(lineOfCsv)
            allDriver.add(parsDriver)
        }
        return allDriver
    }

}