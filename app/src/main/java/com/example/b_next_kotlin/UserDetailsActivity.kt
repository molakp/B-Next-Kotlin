package com.example.b_next_kotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import model.User

class UserDetailsActivity : AppCompatActivity() {
    var UpdateUserDetailsButton: Button? = null
    var ArchiveButton: Button? = null
    var FeedbacksButton: Button? = null
    var UserNameTextView: TextView? = null
    var UsernameEditText: EditText? = null
    var NameEditText: EditText? = null
    var AvailableCarsListView: ListView? = null
    var currentUser: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)
        // ---- assign values to each control of the layout----
        //Buttons
        UpdateUserDetailsButton = findViewById(R.id.homeUserDetailsButton)
        ArchiveButton = findViewById(R.id.UserDetailsArchive)
        FeedbacksButton = findViewById(R.id.FeedbackButtonUserDetails)
        // Edit Text
        UsernameEditText = findViewById(R.id.usernameEditText)
        NameEditText = findViewById(R.id.emailEditText)
        UserNameTextView = findViewById(R.id.userNameTextView)
        currentUser = intent.getSerializableExtra("currentUser") as User?
        Log.d("User BookRide", currentUser.toString())
        UserNameTextView?.setText(
            """${currentUser!!.name} ${currentUser!!.surname}
 Roles: ${currentUser!!.roles}"""
        )
        UsernameEditText?.setText(currentUser!!.username)
        NameEditText?.setText(currentUser!!.name)
        UpdateUserDetailsButton?.setOnClickListener(View.OnClickListener { view: View ->
            val updatedUser = User(currentUser!!.userId, UsernameEditText?.getText().toString())
            updatedUser.name = NameEditText?.getText().toString()
            AndroidNetworking.initialize(applicationContext)
            // pipedream https://eo36hxzz25l7d4r.m.pipedream.net
            AndroidNetworking.put("http://10.0.2.2:8080/user/update") //negli header per il token fare sempre così .addHeaders("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9")
                .addHeaders("Authorization", "Bearer " + MainActivity.Companion.token)
                .addHeaders("accept", "*/*")
                .addHeaders(
                    "accept-encoding",
                    "gzip, deflate, br"
                ) //.addHeaders("content-type","application/json")
                //.setContentType("application/json; charset=utf-8")
                .addApplicationJsonBody(updatedUser) //.addJSONObjectBody(jsonObject) // posting json
                .setPriority(Priority.LOW)
                .build()
                .getAsString(object : StringRequestListener {
                    override fun onResponse(s: String) {
                        // Qua cosa   fare se la richiesta funziona
                        Log.d("Updated user", currentUser.toString())
                        Toast.makeText(this@UserDetailsActivity, s, Toast.LENGTH_LONG).show()
                        // Così ritorniamo alla home dopo l'update
                        val intent = Intent(view.context, MainActivity::class.java)
                        view.context.startActivity(intent)
                    }

                    override fun onError(anError: ANError) {
                        // Qua cosa   fare se la richiesta va in errore
                        Toast.makeText(
                            this@UserDetailsActivity,
                            anError.errorDetail,
                            Toast.LENGTH_LONG
                        ).show()
                        println(anError)
                    }
                })
        })
        ArchiveButton?.setOnClickListener(View.OnClickListener { view: View ->
            val intent = Intent(view.context, ArchiveActivity::class.java)
            intent.putExtra("user", currentUser)
            //intent.putExtra("currentCar",currentCar);
            view.context.startActivity(intent)
        })
        FeedbacksButton?.setOnClickListener(View.OnClickListener { view: View ->
            val intent = Intent(view.context, FeedbacksArchive::class.java)
            intent.putExtra("user", currentUser)
            //intent.putExtra("currentCar",currentCar);
            view.context.startActivity(intent)
        })
    }
}