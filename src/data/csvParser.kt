package data

import data.ColumnIndex.AGE
import data.ColumnIndex.GENDER
import data.ColumnIndex.GOVERNOTATE
import data.ColumnIndex.ID
import data.ColumnIndex.NAME
import data.ColumnIndex.TYPEOFCAR

class csvParser {
     fun parsOneLine (line:String):Driver{
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