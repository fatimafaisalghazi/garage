package data.DistanceData

import data.DistanceData.ColumnIndex.DistanceFromBasra
import data.DistanceData.ColumnIndex.Districtval
import data.DistanceData.ColumnIndex.Governorate

class csvParserForDistance {
    fun parsOneline(line:String):Distance{
        val DistanceInfo =line.split(",")
        return Distance(
            Governorate =DistanceInfo[Governorate],
            Districtval = DistanceInfo[Districtval],
            DIstanceFromBasra = DistanceInfo[DistanceFromBasra].toIntOrNull()

        )
    }
}