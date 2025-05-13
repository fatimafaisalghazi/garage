package data.DriverData

import logic.Repository.DataFromFiles
import java.io.File

class csvFileReader(private val csvFile:File) : DataFromFiles {
   override fun readLinesFromFile():List<String>{
        return csvFile.readLines()
    }

}