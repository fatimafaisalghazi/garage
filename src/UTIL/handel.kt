package UTIL

import data.DriverData.ColumnIndex
import java.io.File


    fun handel() {
        val driverFile = File("drivers.csv")
        try {
            println()

            driverFile.readLines().forEach() { driverInformation ->
                val driverInfo = driverInformation.split(",")
                println( driverInfo[ColumnIndex.NAME])
                println( driverInfo[ColumnIndex.AGE])
                println( driverInfo[ColumnIndex.ID])
                println( driverInfo[ColumnIndex.GENDER])
                println( driverInfo[ColumnIndex.GOVERNOTATE])
            }
        } catch (exption: Exception) {
            println(" cant access thes files ")
        }
    }
