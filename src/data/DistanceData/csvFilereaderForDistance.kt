package data.DistanceData

import logic.Repository.DataFromFiles
import java.io.File

class csvFilereaderForDistance(private val csvFile: File): DataFromFiles {
    override fun readLinesFromFile():List<String>{
        return csvFile.readLines()
    }
}