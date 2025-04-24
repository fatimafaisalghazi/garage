package data.DriverData

import Driver
import data.DriverData.ColumnIndex.AGE
import data.DriverData.ColumnIndex.GENDER
import data.DriverData.ColumnIndex.GOVERNOTATE
import data.DriverData.ColumnIndex.ID
import data.DriverData.ColumnIndex.NAME
import data.DriverData.ColumnIndex.TYPEOFCAR

class csvParser {
     fun parsOneLine (line:String): data.Driver {
        val DrtiverInfo= line.split(",")

        return data.Driver(
            DriverName = DrtiverInfo[NAME],
            DriveAge = DrtiverInfo[AGE].toIntOrNull(),
            Governonate = DrtiverInfo[GOVERNOTATE],
            Gender = DrtiverInfo[GENDER],
            TypeOfCar = DrtiverInfo[TYPEOFCAR],
            DriveID = DrtiverInfo[ID].toIntOrNull()
        )
    }
}