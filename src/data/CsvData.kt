import data.ColumnIndex.AGE
import data.ColumnIndex.GENDER
import data.ColumnIndex.GOVERNOTATE
import data.ColumnIndex.ID
import data.ColumnIndex.NAME
import data.ColumnIndex.TYPEOFCAR
import data.Driver
import logic.DriversRepo
import java.io.File

class csvData(private val  csvfile:File ):DriversRepo {

    override fun getAllDriverInfo(): List<Driver> {
        val allDriver:MutableList<Driver> = mutableListOf()
         csvfile.readLines().forEach{lineOfCsv ->
             val parsDriver =   parsOneLine(lineOfCsv)
            allDriver.add(parsDriver)
        }
        return allDriver
    }
    private fun parsOneLine (line:String):Driver{
        val DrtiverInfo= line.split(",")

        return Driver(
            DriverName = DrtiverInfo[NAME],
            DriveAge = DrtiverInfo[AGE].toIntOrNull(),
            Governonate = DrtiverInfo[GOVERNOTATE],
            Gender = DrtiverInfo[GENDER],
            TypeOfCar =DrtiverInfo[TYPEOFCAR],
            DriveID = DrtiverInfo[ID].toIntOrNull()
        )
    }
}