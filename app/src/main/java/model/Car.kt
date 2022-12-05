package model

import model.Reservation
import model.Feedback
import model.Car
import org.json.JSONObject
import java.io.Serializable
import java.util.*

class Car : Serializable {
    var carId: UUID? = null
    var name: String? = null
    var carModel: String? = null
    var plateNumber: String? = null
    var battery: Int? = null
    var priceHour: Int? = null
    var priceKm: Int? = null
    var availabilityPresent: Boolean? = null
    var user: User? = null
    var reservation: List<Reservation> = ArrayList()
    var feedback: List<Feedback> = ArrayList()

    constructor(
        name: String?,
        carModel: String?,
        plateNumber: String?,
        priceHour: Int?,
        priceKm: Int?,
        availabilityPresent: Boolean?,
        user: User?
    ) {
        this.name = name
        this.carModel = carModel
        this.plateNumber = plateNumber
        this.priceHour = priceHour
        this.priceKm = priceKm
        this.availabilityPresent = availabilityPresent
        this.user = user
    }

    constructor() {}
    constructor(plateNumber: String?) {
        this.plateNumber = plateNumber
    }

    constructor(carId: UUID?) {
        this.carId = carId
    }

    override fun toString(): String {
        return "Car{" +
                "id=" + carId +
                ", name='" + name + '\'' +
                ", carModel='" + carModel + '\'' +
                ", extraData='" + plateNumber + '\'' +
                ", battery=" + battery +
                ", priceHour=" + priceHour +
                ", priceKm=" + priceKm +
                ", availabilityPresent=" + availabilityPresent +
                ", user=" + user +
                ", reservation=" + reservation +
                ", feedback=" + feedback +
                '}'
    }
}