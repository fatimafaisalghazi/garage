import data.ColumnIndex.AGE
import data.ColumnIndex.GENDER
import data.ColumnIndex.GOVERNOTATE
import data.ColumnIndex.ID
import data.ColumnIndex.NAME
import data.ColumnIndex.TYPEOFCAR
import data.Driver
import data.csvParser
import logic.DriversRepo
import java.io.File


class csvData(private val  csvfile:File ,
    private val driverParser: csvParser
):DriversRepo {

    override fun getAllDriverInfo(): List<Driver> {
        val allDriver:MutableList<Driver> = mutableListOf()
         csvfile.readLines().forEach{lineOfCsv ->
             val parsDriver =  driverParser.parsOneLine(lineOfCsv)
            allDriver.add(parsDriver)
        }
        return allDriver
    }

}