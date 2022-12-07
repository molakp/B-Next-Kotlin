package com.example.b_next_kotlin


import model.*
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        Assert.assertEquals(4, (2 + 2).toLong())
    }

    //User Test
    @Test
    fun userConstructorOne() {
        val userTest = User("username", "password")
        assertEquals("username", userTest.username)
        assertEquals("password", userTest.password)
    }

    @Test
    fun userConstructorTwo() {
        val userUUID = UUID.randomUUID()
        val userTest = User(userUUID, "username")
        assertEquals(userUUID.toString(), userTest.userId.toString())
        assertEquals("username", userTest.username)
    }

    @Test
    fun userConstructorComplete() {
        val userUUID = UUID.randomUUID()
        val userTest = User(userUUID, "username")
        //val sDate1 = "31/12/1998"
        /*try {
            Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
            userTest.setBirthDate(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        userTest.feedbacks=(ArrayList<Feedback>())
        userTest.name="TestName"
        userTest.ownedCars=(ArrayList<Car>())
        userTest.roles="ADMIN"
        userTest.password="password"
        userTest.surname="TestSurname"

        assertEquals(userUUID.toString(), userTest.userId?.toString())
        assertEquals("username", userTest.username)
        //assertEquals("Thu Dec 31 00:00:00 CET 1998", userTest.birthDate)
        assertEquals("[]", userTest.feedbacks.toString())
        assertEquals("[]", userTest.ownedCars.toString())
        assertEquals("ADMIN", userTest.roles)
        assertEquals("password", userTest.password)
        assertEquals("TestSurname", userTest.surname)
    }

    /*
    * Car Test
    * */
    @Test
    fun CarPlateNumber() {
        val testCar = Car("TestPlateNumber")
        assertEquals("TestPlateNumber", testCar.plateNumber)
    }

    @Test
    fun CarUUID() {
        val testCar = Car("TestPlateNumber")
        val testUUID = UUID.randomUUID()
        testCar.carId=testUUID
        assertEquals(testUUID.toString(), testCar.carId.toString())
    }

    @Test
    fun CarName() {
        val testCar = Car("TestPlateNumber")
        testCar.name="test"
        assertEquals("test", testCar.name)
    }

    /* Feedback Test*/
    @Test
    fun FeedbackName() {
        val testCar = Car("TestPlateNumber")
        val testUUID = UUID.randomUUID()
        val testUser = User(testUUID, "username")
        val testFeedback = Feedback("testComment", testUser, testCar)
        assertEquals("testComment", testFeedback.comment)
    }

    /* Reservation Test*/
    @Test
    fun ReservationUUID() {
        val testCar = Car("TestPlateNumber")
        val testUUID = UUID.randomUUID()
        val testUser = User(testUUID, "username")
        //val testFeedback = Feedback("testComment", testUser, testCar)
        val testReservation = Reservation(testUUID)
        testReservation.car=testCar
        testReservation.user=testUser
        assertEquals(testUUID.toString(), testReservation.reservationId.toString())
    }

    @Test
    fun ReservationCar() {
        val testCar = Car("TestPlateNumber")
        val testUUID = UUID.randomUUID()
        val testUser = User(testUUID, "username")
        //val testFeedback = Feedback("testComment", testUser, testCar)
        val testReservation = Reservation(testUUID)
        testReservation.car=testCar
        testReservation.user=testUser
        assertEquals(testCar.toString(), testReservation.car.toString())
    }

    @Test
    fun ReservationUser() {
        val testCar = Car("TestPlateNumber")
        val testUUID = UUID.randomUUID()
        val testUser = User(testUUID, "username")
       // val testFeedback = Feedback("testComment", testUser, testCar)
        val testReservation = Reservation(testUUID)
        testReservation.car=testCar
        testReservation.user=testUser
        assertEquals(testUser.toString(), testReservation.user.toString())
    }

    @Test
    fun ReservationDestination() {
        val testCar = Car("TestPlateNumber")
        val testUUID = UUID.randomUUID()
        val testUser = User(testUUID, "username")
        //val testFeedback = Feedback("testComment", testUser, testCar)
        val testReservation = Reservation(testUUID)
        testReservation.car=testCar
        testReservation.user=testUser
        val testPosition = Position(123.00, 123.00)
        testReservation.destination=testPosition
        assertEquals(testPosition.toString(), testReservation.destination.toString())
    }

    @Test
    fun ReservationStartPosition() {
        val testCar = Car("TestPlateNumber")
        val testUUID = UUID.randomUUID()
        val testUser = User(testUUID, "username")
        //val testFeedback = Feedback("testComment", testUser, testCar)
        val testReservation = Reservation(testUUID)
        testReservation.car=testCar
        testReservation.user=(testUser)
        val startPosition = Position(123.00, 123.00)
        testReservation.startPosition=startPosition
        assertEquals(startPosition.toString(), testReservation.startPosition.toString())
    }



}