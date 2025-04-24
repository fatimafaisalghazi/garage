import logic.DriversRepo
import java.io.File


fun main() {
   val fileName ="drivers.csv"
    val csvfile=File(fileName)
    val driversRepo:DriversRepo=csvData(csvfile)
    driversRepo.getAllDriverInfo().also {
        println(" $it")
        println()
    }

    }
