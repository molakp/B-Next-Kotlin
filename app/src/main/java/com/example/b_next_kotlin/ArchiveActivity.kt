/*
 * Copyright (c) 2022.
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.example.b_next_kotlin

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.gson.GsonBuilder
import model.Car
import model.Reservation
import model.User
import org.json.JSONArray
import org.json.JSONObject

class ArchiveActivity : AppCompatActivity() {
    var currentUser: User? = null
    var ReservationListView: ListView? = null

    // ArrayList che contiene la lista di oggetti Feedback appartenenti ad una macchina (macchina passata da un altra activity)
    var reservations = ArrayList<Reservation>()
    var allCars: MutableSet<Car?> = HashSet()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.archive_activity)
        ReservationListView = findViewById(R.id.ReservationsListView)
        // Recupero il token ottenuto dalla pagina di login
        val bundle = intent.extras
        //token = bundle.getString("token");
        Log.d("Archive Activity", MainActivity.Companion.token)
        // Recupero lo user ottenuto dalla pagina di login
        try {
            currentUser = intent.getSerializableExtra("user") as User?
            if (currentUser == null) throw NullPointerException()
        } catch (e: Exception) {
            Log.e("Archive Activity", "Error parsing user ")
            e.printStackTrace()
        }
        Log.d("Archive Activity", "loaded activity user: " + currentUser.toString())


        /*
        *
        * GET REQUEST PER PRENDERCI L'UTENTE AGGIORNATO
        * */

        //          "https://eo36hxzz25l7d4r.m.pipedream.net"
        //          url+"/user/id={id}"
        AndroidNetworking.get(MainActivity.Companion.url + "user/id=" + currentUser!!.userId.toString()) //negli header per il token fare sempre così .addHeaders("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9")
            .addHeaders("Authorization", "Bearer " + MainActivity.Companion.token)
            .addHeaders("accept", "*/*")
            .addHeaders(
                "accept-encoding",
                "gzip, deflate, br"
            ) //.addPathParameter("id", currentUser.getUserId().toString())
            .addQueryParameter("limit", "10") //.addHeaders("content-type","application/json")
            //.setContentType("application/json; charset=utf-8")
            //.addApplicationJsonBody(DateChooseEditText.getText())
            //.addJSONObjectBody(jsonObject) // posting json
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.println(Log.INFO, "Archive Activity", "Getting user")


                    // Qua cosa   fare se la richiesta funziona
                    try {
                        //token = (String) response.get("token");
                        val gson = GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                            .create()
                        currentUser = gson.fromJson(
                            response.toString(),
                            User::class.java
                        ) // deserializes json into target2
                        //System.out.println("####################################\n"+currentUser.toString());
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    reservations = currentUser!!.reservations as ArrayList<Reservation>
                    Log.println(
                        Log.INFO,
                        "Archive Activity",
                        "Success getting user reservations$reservations"
                    )
                    //updateReservationsWithCars(reservations);
                    val customViewAdapter =
                        CustomReservationAdapter(this@ArchiveActivity, reservations, currentUser)

                    // set the CustomViewAdapter for ListView
                    ReservationListView?.setAdapter(customViewAdapter)
                }

                override fun onError(error: ANError) {
                    // Qua cosa   fare se la richiesta va in errore
                    Toast.makeText(this@ArchiveActivity, error.errorDetail, Toast.LENGTH_LONG)
                        .show()
                    println(error)
                    Log.println(Log.ERROR, "Archive Activity", error.errorDetail)
                }
            })
    }

    //Set <Car> allCars= new HashSet<>();
    fun updateReservationsWithCars(reservations: ArrayList<Reservation>) {
        // Set <Car> allCars= new HashSet<>();
        AndroidNetworking.get(MainActivity.Companion.url + "cars") //negli header per il token fare sempre così .addHeaders("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9")
            .addHeaders("Authorization", "Bearer " + MainActivity.Companion.token)
            .addHeaders("accept", "*/*")
            .addHeaders(
                "accept-encoding",
                "gzip, deflate, br"
            ) //.addPathParameter("id", currentUser.getUserId().toString())
            //.addQueryParameter("limit", "10")
            //.addHeaders("content-type","application/json")
            //.setContentType("application/json; charset=utf-8")
            //.addApplicationJsonBody(DateChooseEditText.getText())
            //.addJSONObjectBody(jsonObject) // posting json
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    Log.println(Log.INFO, "Archive Activity", "Getting all cars")
                    var currentCar: Car? = null
                    for (i in 0 until response.length()) {
                        try {
                            //token = (String) response.get("token");
                            val gson = GsonBuilder()
                                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                .create()
                            currentCar = gson.fromJson(
                                response.toString(),
                                Car::class.java
                            ) // deserializes json into target2
                            allCars.add(currentCar)
                            //System.out.println("####################################\n"+currentUser.toString());
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    Log.println(Log.INFO, "Archive Activity", "Success getting all cars$allCars")
                    for (currentRes in reservations) {
                        for (iterCar in allCars) {
                            if (iterCar!!.reservation.contains(currentRes)) {
                                currentRes.car = iterCar
                            }
                        }
                    }
                }

                override fun onError(error: ANError) {
                    // Qua cosa   fare se la richiesta va in errore
                    Toast.makeText(this@ArchiveActivity, error.errorDetail, Toast.LENGTH_LONG)
                        .show()
                    println(error)
                    Log.println(Log.ERROR, "Archive Activity", error.errorDetail)
                }
            })


        // return reservations;
    }
}