package com.example.b_next_kotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.example.bnext.CustomViewAdapter
import com.google.gson.GsonBuilder
import model.Car
import model.Feedback
import model.User
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class StatsCarActivity : AppCompatActivity() {
    //String token ="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcm92YSIsImlhdCI6MTY2Nzc1MTM3OSwiZXhwIjoxNjY3NzY5Mzc5fQ.JpSoP74LAP3CWYweJ__177tYVCVzb1bHQmwwbr6MMP15uZgjDm8rTkhz718K-39h01QXdZ_mK_z0N5rOuMyrGQ";
    var carID: String? = null
    var userID: String? = null
    var carName: TextView? = null
    var carPlateNumber: TextView? = null
    var priceHour: TextView? = null
    var priceKM: TextView? = null
    var bookRideButton: Button? = null
    var commentSection: ListView? = null
    var carImage: ImageView? = null

    // ArrayList che contiene la lista di oggetti Feedback appartenenti ad una macchina (macchina passata da un altra activity)
    val feedbacks = ArrayList<Feedback>()
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats_car)
        title = "Stats car"

        // Recupero il token ottenuto dalla pagina di login
        intent.extras
        //token = bundle.getString("token");
        val currentCar = intent.getSerializableExtra("currentCar") as Car?
        val currentUser = intent.getSerializableExtra("user") as User?


        // ---- assign values to each control of the layout----
        carName = findViewById(R.id.carName)
        carPlateNumber = findViewById(R.id.carPlateNumber)
        priceHour = findViewById(R.id.priceHour)
        priceKM = findViewById(R.id.priceKM)
        bookRideButton = findViewById(R.id.bookRideButton)
        commentSection = findViewById(R.id.commentSection)
        carImage = findViewById(R.id.carImage)

        // modify text
        carName?.text = currentCar!!.name
        carPlateNumber?.setText("Plate number: " + currentCar.plateNumber)
        priceKM?.append(currentCar.priceKm.toString())
        priceHour?.append(currentCar.priceHour.toString())
        val src =
            (currentCar.name?.lowercase(Locale.getDefault()) ?: "") + "_" + currentCar.carModel?.lowercase(
                Locale.getDefault()
            )
        val drawableId = this.resources.getIdentifier(src, "drawable", this.packageName)
        carImage?.setImageResource(drawableId)

        //carImage.setImageURI(Uri.parse("lamborghini_hurricane"));
        carID = currentCar.carId.toString()
        //System.out.println(currentCar);
        userID = currentUser!!.userId.toString()

        // Populate the UI with Fast Android Networking Library
        AndroidNetworking.initialize(applicationContext)
        AndroidNetworking.initialize(applicationContext)
        // get the list of objects of type Feedback
        AndroidNetworking.get(MainActivity.url + "feedbacks/" + carID) //negli header per il token fare sempre così .addHeaders("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9")
            .addHeaders("Authorization", "Bearer " + MainActivity.token)
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                // Qua cosa   fare se la richiesta funziona
                override fun onResponse(response: JSONArray) {
                    for (i in 0 until response.length()) {
                        try {
                            // recupero l'oggetto
                            val resp = response[i] as JSONObject
                            val gson = GsonBuilder()
                                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                .create()
                            //System.out.println("###############\n"+response.get(i));
                            //Car car = new Car(UUID.fromString(carID));
                            val user = User(
                                UUID.fromString(userID),
                                resp["nomeUtente"].toString(),
                                resp["cognomeUtente"].toString()
                            )
                            //Feedback feed = new Feedback(UUID.fromString(resp.get("idFeedback").toString()), resp.get("comment").toString(), car, user);
                            val feed = gson.fromJson(
                                resp.toString(),
                                Feedback::class.java
                            ) // deserializes json into target2
                            feed.user = user
                            feedbacks.add(feed)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                    // Now create the instance of the CustomViewAdapter and pass
                    // the context and arrayList created above
                    val customViewAdapter = CustomViewAdapter(this@StatsCarActivity, feedbacks)

                    // set the CustomViewAdapter for ListView
                    commentSection?.setAdapter(customViewAdapter)
                    Log.println(Log.INFO, "Feedbacks: ", feedbacks.toString())
                }

                override fun onError(error: ANError) {
                    // handle error
                    // Qua cosa   fare se la richiesta va in errore
                    Toast.makeText(this@StatsCarActivity, error.errorCode, Toast.LENGTH_LONG).show()
                    println(error)
                }
            })
        priceHour?.setTypeface(null, Typeface.BOLD)
        priceKM?.setTypeface(null, Typeface.BOLD)


        //carName.setTypeface(null, Typeface.BOLD_ITALIC);
        //carName.setTypeface(null, Typeface.ITALIC);
        //carName.setTypeface(null, Typeface.NORMAL);*/
        bookRideButton?.setOnClickListener { view: View ->
            /* Per ora va direttamente,
            dopo va fatto che si procede a quella dopo
            selezionando una macchina e passando all'activity
            seguente le informazioni
            * */
            val intent = Intent(view.context, BookActivity::class.java)
            // Quindi posso passare il token di autenticazione all'altra activity
            //intent.putExtra("token", token);
            intent.putExtra("currentUser", currentUser)
            intent.putExtra("currentCar", currentCar)
            view.context.startActivity(intent)
        }
    }
}