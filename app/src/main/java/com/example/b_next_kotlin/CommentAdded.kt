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
import model.Car
import model.User
import java.util.*

class CommentAdded : AppCompatActivity() {
    var carName: TextView? = null
    var carPlateNumber: TextView? = null
    var userToComment: TextView? = null
    var feedbackSection: EditText? = null
    var carImage: ImageView? = null
    var userImage: ImageView? = null
    var addFeedback: Button? = null
    var home: Button? = null
    var currentUser: User? = null
    var currentCar: Car? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_added)
        currentCar = intent.getSerializableExtra("currentCar") as Car?
        currentUser = intent.getSerializableExtra("currentUser") as User?

        // ---- assign values to each control of the layout----
        carName = findViewById(R.id.carName)
        carPlateNumber = findViewById(R.id.carPlateNumber)
        userToComment = findViewById(R.id.userToComment)
        carImage = findViewById(R.id.carImage)
        addFeedback = findViewById(R.id.addFeedback)
        home = findViewById(R.id.home)


        // set image of car
        val src =
            currentCar!!.name?.lowercase(Locale.getDefault()) + "_" + currentCar!!.carModel?.lowercase(
                Locale.getDefault()
            )
        val drawableId = this.resources.getIdentifier(src, "drawable", this.packageName)
        carImage?.setImageResource(drawableId)

        // modify text
        carName?.setText(currentCar!!.name)
        carPlateNumber?.setText("Plate number: " + currentCar!!.plateNumber)

        // assign text to a feedback

        // Populate the UI with Fast Android Networking Library
        AndroidNetworking.initialize(applicationContext)
        addFeedback?.setOnClickListener(View.OnClickListener { view: View ->
            //Car car = new Car(UUID.fromString("1566ee1b-1668-4815-8c2b-aeebd169b13b"));
            //User user = new User(UUID.fromString("2e34e2e0-c9b0-4ebd-83cb-2cffde2b333d"),"prova");
            val intent = Intent(view.context, AddComment::class.java)
            intent.putExtra("currentUser", currentUser)
            intent.putExtra("currentCar", currentCar)
            view.context.startActivity(intent)
        })
        home?.setOnClickListener(View.OnClickListener { view: View ->
            val intent = Intent(view.context, BookRide::class.java)
            intent.putExtra("currentUser", currentUser)
            intent.putExtra("currentCar", currentCar)
            view.context.startActivity(intent)
        })
    }
}