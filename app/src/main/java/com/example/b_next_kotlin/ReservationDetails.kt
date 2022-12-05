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
import model.Reservation
import model.User

/*
 * Copyright (c) 2022.
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */   class ReservationDetails : AppCompatActivity() {
    var dateTextView: TextView? = null
    var timeTextView: TextView? = null
    var deleteButton: Button? = null
    var homeButton: Button? = null
    var AddFeedbackDetailsHomeButton: Button? = null

    // ArrayList che contiene la lista di oggetti Feedback appartenenti ad una macchina (macchina passata da un altra activity)
    var currentReservation: Reservation? = null
    var currentUser: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_details)
        dateTextView = findViewById(R.id.ReservationDetailsCommentTextView)
        timeTextView = findViewById(R.id.ReservationTimeTextView)
        deleteButton = findViewById(R.id.ReservationDetailsDeleteButton)
        homeButton = findViewById(R.id.ReservationDetailsHomeButton)
        AddFeedbackDetailsHomeButton = findViewById(R.id.AddFeedbackDetailsHomeButton)

        // Recupero il token ottenuto dalla pagina di login
        val bundle = intent.extras
        //token = bundle.getString("token");
        Log.d("Archive Activity", MainActivity.Companion.token)
        // Recupero lo user ottenuto dalla pagina di login
        try {
            currentUser = intent.getSerializableExtra("user") as User?
            currentReservation = intent.getSerializableExtra("currentReservation") as Reservation?
            if (currentUser == null || currentReservation == null) throw NullPointerException()
        } catch (e: Exception) {
            Log.e("Reservation Details", "Error parsing")
            e.printStackTrace()
        }
        Log.d(
            "Reservation Details",
            "loaded activity reservation: " + currentReservation.toString()
        )

        /*
        SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss yyyy",
                Locale.ENGLISH);
        Date date = null;
        String DateToParseString = "";
        try {
            //Log.d("Reservation Details", String.valueOf(currentReservation.getStartOfBook().toInstant().getEpochSecond()));
            DateToParseString = currentReservation.getStartOfBook();
            //Wed Jul 27 15:00:00 GMT+02:00 2022
            String[] chunks = DateToParseString.split(" ");
            //Date parsedDate  = sdf.parse(chunks[0]+" "+chunks[1]+ " " + chunks[2] + " " + chunks[3] + " " + chunks[4]);


            //SimpleDateFormat printParser = new SimpleDateFormat("MMM d yyyy HH:mm:ss");



            dateTextView.setText(chunks[0]+" "+chunks[1]+ " " + chunks[2] +" "+ chunks[5] );
            timeTextView.setText(chunks[3]);

          Log.d("Reservation Details","setted dates");

        } catch (Exception e) {
            Log.e("Error parsing date", currentReservation.getStartOfBook());
            e.printStackTrace();
        }
        */
        val chunks = currentReservation!!.startOfBook?.split("T")?.toTypedArray()
        dateTextView?.setText(chunks?.get(0) ?: "")
        //Imposto l'ora, eliminando i secondo che sono inutili
        timeTextView?.setText(chunks?.get(1)?.substring(0, chunks[1].length.minus(3) ?: 0) ?: "")
        homeButton?.setOnClickListener(View.OnClickListener { view: View ->
            val intent = Intent(view.context, BookRide::class.java)
            intent.putExtra("currentUser", currentUser)
            //intent.putExtra("currentReservation",currentReservation);
            view.context.startActivity(intent)
        })
        deleteButton?.setOnClickListener(View.OnClickListener { view: View ->
            AndroidNetworking.initialize(applicationContext)
            AndroidNetworking.delete(MainActivity.Companion.url + "reservations/delete=" + currentReservation!!.reservationId.toString())
                .addHeaders("Authorization", "Bearer " + MainActivity.Companion.token)
                .addHeaders("accept", "*/*")
                .addHeaders("accept-encoding", "gzip, deflate, br")
                .setPriority(Priority.LOW)
                .build()
                .getAsString(object : StringRequestListener {
                    override fun onResponse(s: String) {
                        // Toast.makeText(ReservationDetails.this, s, Toast.LENGTH_LONG).show();
                        val intent = Intent(view.context, ArchiveActivity::class.java)
                        intent.putExtra("user", currentUser)
                        //intent.putExtra("currentReservation",currentReservation);
                        view.context.startActivity(intent)
                    }

                    override fun onError(anError: ANError) {
                        // Qua cosa   fare se la richiesta va in errore
                        Toast.makeText(
                            this@ReservationDetails,
                            anError.errorDetail,
                            Toast.LENGTH_LONG
                        ).show()
                        println(anError)
                    }
                }
                )
        })
    } /*
         *
         * GET REQUEST PER PRENDERCI L'UTENTE AGGIORNATO
         * */
    //          "https://eo36hxzz25l7d4r.m.pipedream.net"
    //          url+"reservations/delete="
}