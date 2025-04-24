import data.DriverData.csvParser
import logic.DriversRepo
import data.DriverData.csvFileReader

class csvData(private val  CsvfileReader: csvFileReader,
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

//blueprint
data class Driver(
    val DriverName:String,
    val DriveAge: Int?,
    val Governonate:String,
    val Gender:String,
    val TypeOfCar:String,
    val DriveID:Int?,

    )