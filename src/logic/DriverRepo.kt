package logic

import data.Driver


interface DriversRepo {
        fun getAllDriverInfo():List<Driver>

    }
