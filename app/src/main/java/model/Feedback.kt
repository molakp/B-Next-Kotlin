package model

import model.Reservation
import model.Feedback
import model.Car
import org.json.JSONObject
import java.io.Serializable
import java.util.*

class Feedback : Serializable {
    var idFeedback: UUID
    var comment: String
    var user: User
    var car: Car

    constructor(idFeedback: UUID, comment: String, user: User, car: Car) {
        this.idFeedback = idFeedback
        this.comment = comment
        this.user = user
        this.car = car
    }

    constructor(comment: String, user: User, car: Car) {
        this.comment = comment
        this.user = user
        this.car = car
        idFeedback = UUID.randomUUID()
    }

    constructor(idFeedback: UUID, comment: String, car: Car, user: User) {
        this.comment = comment
        this.car = car
        this.user = user
        this.idFeedback = idFeedback
    }

    override fun toString(): String {
        return "Feedback{" +
                "idFeedback=" + idFeedback +
                ", comment='" + comment + '\'' +
                ", user=" + user +
                ", car=" + car +
                '}'
    }
}