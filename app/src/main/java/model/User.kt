package model

import model.Reservation
import model.Feedback
import model.Car
import org.json.JSONObject
import java.io.Serializable
import java.util.*

class User : Serializable {
    var userId: UUID? = null
    var name: String? = null
    var surname: String? = null
    var birthDate: String? = null

    constructor(
        name: String?,
        surname: String?,
        birthDate: String?,
        username: String?,
        password: String?
    ) {
        this.name = name
        this.surname = surname
        this.birthDate = birthDate
        this.username = username
        this.password = password
    }

    var username: String? = null
    var password: String? = null
    var active = 0

    // Per inserire più ruoli, inserirli tutti nella stessa stringa divisi con ","
    var permissions: String? = null
    var roles: String? = null
    var ownedCars: List<Car>? = ArrayList()
    var reservations: List<Reservation>? = ArrayList()
    var feedbacks: List<Feedback>? = ArrayList()

    constructor(
        userId: UUID?,
        name: String?,
        surname: String?,
        birthDate: String?,
        username: String?,
        password: String?,
        active: Int,
        permissions: String?,
        roles: String?,
        ownedCars: List<Car>?,
        reservations: List<Reservation>?,
        feedbacks: List<Feedback>?
    ) {
        /*
        * Complete constructor
        * */
        this.userId = userId
        this.name = name
        this.surname = surname
        this.birthDate = birthDate
        this.username = username
        this.password = password
        this.active = active
        this.permissions = permissions
        this.roles = roles
        this.ownedCars = ownedCars
        this.reservations = reservations
        this.feedbacks = feedbacks
    }

    constructor(
        userId: UUID?,
        name: String?,
        surname: String?,
        birthDate: String?,
        username: String?,
        password: String?,
        active: Int,
        permissions: String?,
        roles: String?
    ) {
        this.userId = userId
        this.name = name
        this.surname = surname
        this.birthDate = birthDate
        this.username = username
        this.password = password
        this.active = active
        this.permissions = permissions
        this.roles = roles
    }

    constructor(userId: UUID?, username: String?) {
        /*FOr update username*/
        this.userId = userId
        this.username = username
    }

    constructor(username: String?, password: String?) {
        this.username = username
        this.password = password
    }

    constructor(userId: UUID?, name: String?, surname: String?) {
        this.userId = userId
        this.name = name
        this.surname = surname
    }

    constructor(user: JSONObject) {
        //System.out.println("Ecco l'utente*********************+\n"+user);
        userId = UUID.fromString(user["userId"].toString())
        name = user["name"].toString()
        surname = user["surname"].toString()
        birthDate =
            user["birthDate"].toString() //new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.sssZ").parse(user.get("birthDate").toString());
        username = user["username"].toString()
        password = user["password"].toString()
        active = user["active"] as Int
        permissions = user["permissions"].toString()
        roles = user["roles"].toString()
        ownedCars = null
        reservations = null
        feedbacks = null
    }

    constructor() {}
    constructor(userId: UUID?) {
        this.userId = userId
    }

    override fun toString(): String {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthDate=" + birthDate +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active +
                ", permissions='" + permissions + '\'' +
                ", roles='" + roles + '\'' +
                ", ownedCars=" + ownedCars +
                ", reservations=" + reservations +
                ", feedbacks=" + feedbacks +
                '}'
    }
}