package model

import model.Reservation
import model.Feedback
import model.Car
import org.json.JSONObject
import java.io.Serializable
import java.util.*

class Reservation : Serializable {
    var reservationId: UUID
    var startOfBook: String? = null
    var endOfBook: String? = null
    var user: User? = null
    var car: Car? = null
    var destination: Position? = null
    var startPosition: Position? = null

    constructor(reservationId: UUID) {
        this.reservationId = reservationId
    }

    constructor(
        reservationId: UUID,
        startOfBook: String?,
        endOfBook: String?,
        user: User?,
        car: Car?,
        destination: Position?,
        startPosition: Position?
    ) {
        /*
        * Provide reservation ID
        * */
        this.startOfBook = startOfBook
        this.endOfBook = endOfBook
        this.user = user
        this.car = car
        this.destination = destination
        this.startPosition = startPosition
        this.reservationId = reservationId
    }

    constructor(
        startOfBook: String?,
        endOfBook: String?,
        user: User?,
        car: Car?,
        destination: Position?,
        startPosition: Position?
    ) {
        /*
        * No need to provide ID
        * */
        this.startOfBook = startOfBook
        this.endOfBook = endOfBook
        this.user = user
        this.car = car
        this.destination = destination
        this.startPosition = startPosition
        reservationId = UUID.randomUUID()
    }

    override fun toString(): String {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", startOfBook=" + startOfBook +
                ", endOfBook=" + endOfBook +
                ", user=" + user +
                ", car=" + car +
                ", destination=" + destination +
                ", startPosition=" + startPosition +
                '}'
    }
}