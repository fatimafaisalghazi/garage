package data.DriverData

import java.io.File

class csvFileReader(private val csvFile:File) {
    fun readLinesFromFile():List<String>{
        return csvFile.readLines()
    }

}