package logic.usecase

import logic.entity.Passenger

class RegisterPassengerUseCase {
    fun execute(name: String, phone: String, governorate: String, district: String): Passenger {
        return Passenger(name, phone, governorate, district)
    }
}
