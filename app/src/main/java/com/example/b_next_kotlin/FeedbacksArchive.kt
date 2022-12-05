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
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.gson.GsonBuilder
import model.Feedback
import model.User
import org.json.JSONObject

class FeedbacksArchive : AppCompatActivity() {
    var currentUser: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedbacks_archive)
        val FeedbacksListView = findViewById<ListView>(R.id.ReservationsListView)
        // ArrayList che contiene la lista di oggetti Feedback appartenenti ad una macchina (macchina passata da un altra activity)
        val feedbacksSet: MutableSet<Feedback> = HashSet()
        val bundle = intent.extras
        //token = bundle.getString("token");
        Log.d("Feedbacks Activity", MainActivity.Companion.token)
        // Recupero lo user ottenuto dalla pagina di login
        try {
            currentUser = intent.getSerializableExtra("user") as User?
            if (currentUser == null) throw NullPointerException()
            Log.d("Archive Activity", "loaded activity user: " + currentUser.toString())
        } catch (e: Exception) {
            Log.e("Archive Activity", "Error parsing user ")
            e.printStackTrace()
        }
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
                    currentUser!!.feedbacks?.let { feedbacksSet.addAll(it) }
                    Log.println(
                        Log.INFO,
                        "Feedbacks Activity",
                        "Success getting user feedbacks$feedbacksSet"
                    )
                    val tempList = ArrayList<Feedback>()
                    tempList.addAll(feedbacksSet)
                    Log.println(
                        Log.INFO,
                        "Feedbacks Activity",
                        "Success getting user feedbacks$tempList"
                    )
                    val customViewAdapter =
                        CustomFeedbacksAdapter(this@FeedbacksArchive, tempList, currentUser)

                    // set the CustomViewAdapter for ListView
                    FeedbacksListView.adapter = customViewAdapter
                }

                override fun onError(error: ANError) {
                    // Qua cosa   fare se la richiesta va in errore
                    Toast.makeText(this@FeedbacksArchive, error.errorDetail, Toast.LENGTH_LONG)
                        .show()
                    println(error)
                    Log.println(Log.ERROR, "Feedbacks Archive", error.errorDetail)
                }
            })
    }
}