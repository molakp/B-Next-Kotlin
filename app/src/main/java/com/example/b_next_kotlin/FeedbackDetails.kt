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

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import model.Feedback
import model.User

class FeedbackDetails : AppCompatActivity() {
    var commentTextView: TextView? = null
    var IDTextView: TextView? = null
    var deleteButton: Button? = null
    var homeButton: Button? = null

    // ArrayList che contiene la lista di oggetti Feedback appartenenti ad una macchina (macchina passata da un altra activity)
    var currentFeedback: Feedback? = null
    var currentUser: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_details)
        commentTextView = findViewById(R.id.FeedbackDetailsCommentTextView)
        IDTextView = findViewById(R.id.FeedbackDetailsIDTextView)
        deleteButton = findViewById(R.id.FeedbackDetailsDeleteButton)
        homeButton = findViewById(R.id.FeedbackDetailsHomeButton)
        val bundle = intent.extras
        //token = bundle.getString("token");
        Log.d("Archive Activity", MainActivity.Companion.token)
        // Recupero lo user ottenuto dalla pagina di login
        try {
            currentUser = intent.getSerializableExtra("user") as User?
            currentFeedback = intent.getSerializableExtra("currentFeedback") as Feedback?
            if (currentUser == null || currentFeedback == null) throw NullPointerException()
        } catch (e: Exception) {
            Log.e("Feedback Details", "Error parsing")
            e.printStackTrace()
        }
        Log.d("Feedback Details", "loaded activity Feedback details: " + currentFeedback.toString())

        /*
        * QUI IMPOSTO I VALORI DEI CAMPI DI TESTO
        * */commentTextView?.setText(currentFeedback!!.comment)
        IDTextView?.setText(currentFeedback!!.idFeedback.toString())
        homeButton?.setOnClickListener(View.OnClickListener { view: View ->
            val intent = Intent(view.context, BookRide::class.java)
            intent.putExtra("currentUser", currentUser)
            //intent.putExtra("currentReservation",currentReservation);
            view.context.startActivity(intent)
        })
        deleteButton?.setOnClickListener(View.OnClickListener { view: View ->
            AndroidNetworking.initialize(applicationContext)
            AndroidNetworking.delete(
                MainActivity.Companion.url + "feedbacks/" + currentFeedback!!.idFeedback
                    .toString()
            )
                .addHeaders("Authorization", "Bearer " + MainActivity.Companion.token)
                .addHeaders("accept", "*/*")
                .addHeaders("accept-encoding", "gzip, deflate, br")
                .setPriority(Priority.LOW)
                .build()
                .getAsString(object : StringRequestListener {
                    override fun onResponse(s: String) {
                        // Toast.makeText(ReservationDetails.this, s, Toast.LENGTH_LONG).show();
                        val intent = Intent(view.context, FeedbacksArchive::class.java)
                        intent.putExtra("user", currentUser)
                        //intent.putExtra("currentReservation",currentReservation);
                        view.context.startActivity(intent)
                    }

                    override fun onError(anError: ANError) {
                        // Qua cosa   fare se la richiesta va in errore
                        Toast.makeText(this@FeedbackDetails, anError.errorDetail, Toast.LENGTH_LONG)
                            .show()
                        println(anError)
                    }
                }
                )
        })
    }
}