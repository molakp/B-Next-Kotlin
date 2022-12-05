package com.example.b_next_kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.google.gson.Gson
import model.Car
import model.Feedback
import model.User
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class AddComment : AppCompatActivity() {
    var carName: TextView? = null
    var carPlateNumber: TextView? = null
    var userToComment: TextView? = null
    var feedbackSection: EditText? = null
    var carImage: ImageView? = null
    var userImage: ImageView? = null
    var addFeedback: Button? = null
    var currentUser: User? = null
    var currentCar: Car? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_comment)
        currentCar = intent.getSerializableExtra("currentCar") as Car?
        currentUser = intent.getSerializableExtra("currentUser") as User?
        // ---- assign values to each control of the layout----
        carName = findViewById(R.id.carName)
        carPlateNumber = findViewById(R.id.carPlateNumber)
        userToComment = findViewById(R.id.userToComment)
        feedbackSection = findViewById(R.id.feedbackSection)
        carImage = findViewById(R.id.carImage)
        userImage = findViewById(R.id.userImage)
        addFeedback = findViewById(R.id.addFeedback)


        // set image of car
        val src =
            currentCar!!.name?.lowercase(Locale.getDefault()) + "_" + currentCar!!.carModel?.lowercase(
                Locale.getDefault()
            )
        val drawableId = this.resources.getIdentifier(src, "drawable", this.packageName)
        carImage?.setImageResource(drawableId)

        // modify text
        carName?.setText(currentCar!!.name)
        carPlateNumber?.setText(currentCar!!.plateNumber)
        userToComment?.append(" " + currentUser!!.username)
        // assign text to a feedback

        // Populate the UI with Fast Android Networking Library
        AndroidNetworking.initialize(applicationContext)
        addFeedback?.setOnClickListener(View.OnClickListener { view: View ->
            val jsonObject = JSONObject()
            //Car car = new Car(UUID.fromString("1566ee1b-1668-4815-8c2b-aeebd169b13b"));
            //User user = new User(UUID.fromString("2e34e2e0-c9b0-4ebd-83cb-2cffde2b333d"),"prova");
            val feedbackFromTextView =
                Feedback(feedbackSection?.getText().toString(), currentUser!!, currentCar!!)
            try {
                jsonObject.put("comment", feedbackSection?.getText().toString())
                jsonObject.put("user", JSONObject(Gson().toJson(currentUser)))
                jsonObject.put("car", JSONObject(Gson().toJson(currentCar)))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            println("**************\n$jsonObject")
            AndroidNetworking.post(MainActivity.Companion.url + "feedbacks/addFeed") //negli header per il token fare sempre così .addHeaders("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9")
                .addHeaders("Authorization", "Bearer " + MainActivity.Companion.token)
                .addHeaders("accept", "*/*")
                .addHeaders(
                    "accept-encoding",
                    "gzip, deflate, br"
                ) //.addHeaders("content-type","application/json")
                //.setContentType("application/json; charset=utf-8")
                //.addApplicationJsonBody(DateChooseEditText.getText())
                .addJSONObjectBody(jsonObject) // posting json
                .setPriority(Priority.LOW)
                .build()
                .getAsString(object : StringRequestListener {
                    override fun onResponse(s: String) {
                        // Qua cosa   fare se la richiesta funziona
                        //Log.d("Updated user", currentUser.toString());
                        // Ripulisco l'edit text contenete il feedback appena aggiunto
                        feedbackSection?.setText("")

                        //vado nella pagia che mi conferma l'aggiunta del commento
                        val intent = Intent(view.context, CommentAdded::class.java)
                        intent.putExtra("currentUser", currentUser)
                        intent.putExtra("currentCar", currentCar)
                        view.context.startActivity(intent)
                        // Così ritorniamo alla home dopo l'update
                    }

                    override fun onError(anError: ANError) {
                        // Qua cosa   fare se la richiesta va in errore
                        //Toast.makeText(BookRide.this, anError.getErrorDetail(), Toast.LENGTH_LONG).show();
                        println(anError)
                    }
                })
        })
    }
}