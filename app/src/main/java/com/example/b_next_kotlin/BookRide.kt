package com.example.b_next_kotlin

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.google.gson.GsonBuilder
import model.Car
import model.User
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class BookRide : AppCompatActivity() {
    //String token = "";
    var SearchRideButton: Button? = null
    var BookRideButton: Button? = null
    var UserNameTextView: TextView? = null
    var PriceKmEditText: EditText? = null
    var DestinationEditText: EditText? = null
    var DateChooseEditText: EditText? = null
    var TimeChooseEditText: EditText? = null
    var AvailableCarsListView: ListView? = null
    var userAvatar: ImageView? = null
    var currentUser: User? = null
    var myFormat = "yyyy/MM/dd"
    val myCalendar = Calendar.getInstance()!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_ride)

        // Recupero il token ottenuto dalla pagina di login
        val bundle = intent.extras
        //token = bundle.getString("token");
        Log.d("Token BookRide", MainActivity.Companion.token)
        // Recupero lo user ottenuto dalla pagina di login
        currentUser = intent.getSerializableExtra("currentUser") as User?
        val currentTime = Calendar.getInstance().time
        Log.d("User BookRide", currentUser.toString())

        // ---- assign values to each control of the layout----
        //Buttons
        SearchRideButton = findViewById(R.id.SearchRideButton)
        //BookRideButton = findViewById(R.id.bookRideButton);

        // Text Views
        UserNameTextView = findViewById(R.id.UserNameTextView)
        UserNameTextView?.setText(
            """${currentUser!!.name} ${currentUser!!.surname}
 ${currentUser!!.username}"""
        )

        // Edit Text

        //PriceKmEditText = findViewById(R.id.PriceKmEditText);
        //DestinationEditText = findViewById(R.id.DestinationEditText);
        DateChooseEditText = findViewById(R.id.DateChooseEditText)
        TimeChooseEditText = findViewById(R.id.TimeChooseEditText)

        // setta la data attuale come ricerca di default
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val currentDate = sdf.format(Date())
        val parts = currentDate.split(" ").toTypedArray()
        DateChooseEditText?.setText(parts[0])
        TimeChooseEditText?.setText(parts[1])
        val datePicker = OnDateSetListener { view: DatePicker?, year: Int, month: Int, day: Int ->
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = month
            myCalendar[Calendar.DAY_OF_MONTH] = day
            updateLabelEndDate()
        }
        val hourPicker = OnTimeSetListener { view: TimePicker?, hour: Int, minute: Int ->
            myCalendar[Calendar.HOUR_OF_DAY] = hour
            myCalendar[Calendar.MINUTE] = minute
            updateLabelEndTime()
        }
        TimeChooseEditText?.setOnClickListener(View.OnClickListener { view: View? ->
            TimePickerDialog(
                this, hourPicker,
                myCalendar[Calendar.HOUR_OF_DAY], myCalendar[Calendar.MINUTE], true
            ).show()
        })
        DateChooseEditText?.setOnClickListener(View.OnClickListener { view: View? ->
            DatePickerDialog(
                this,
                datePicker,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        })


        // List View
        AvailableCarsListView = findViewById(R.id.ReservationsListView)


        // Image Buttons
        userAvatar = findViewById(R.id.userAvatar)


        //click listener rimpiazzato con una lambda
        SearchRideButton?.setOnClickListener(View.OnClickListener { view: View? ->
            AndroidNetworking.initialize(applicationContext)
            // pipedream https://eo36hxzz25l7d4r.m.pipedream.net
            /*Example request as string JSON parsable
            * {
                 "startOfBook" : "2020-07-27T13:31:00",
                  "endOfBook": "2020-07-27T14:10:00"
               }
            * */
            // ArrayList che contiene la lista di oggetti Feedback appartenenti ad una macchina (macchina passata da un altra activity)
            val availableCars = ArrayList<Car>()
            val availableCarsSet: MutableSet<Car> = HashSet()
            val format: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
            var date: Date? = null
            var DateToParseString = ""
            try {
                DateToParseString =
                    DateChooseEditText?.getText().toString() + "T" + TimeChooseEditText?.getText()
                        .toString() + ":00"
                date = format.parse(DateToParseString)
            } catch (e: ParseException) {
                Log.e("Error parsing date", DateChooseEditText?.getText().toString())
                e.printStackTrace()
            }
            /*
            * Il backend vuole le date
            * fatte così "2022-07-27T15:00:00"
            *
            * */
            val jsonObject = JSONObject()
            try {
                jsonObject.put("startOfBook", DateToParseString)
                jsonObject.put("endOfBook", DateToParseString)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            //localhost:8080/reservations/availableCars
            // https://eo36hxzz25l7d4r.m.pipedream.net
            AndroidNetworking.post(MainActivity.Companion.url + "reservations/availableCars") //negli header per il token fare sempre così .addHeaders("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9")
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
                .getAsJSONArray(object : JSONArrayRequestListener {
                    override fun onResponse(response: JSONArray) {
                        // Qua cosa   fare se la richiesta funziona
                        Log.d("Updated user", currentUser.toString())
                        //Toast.makeText(BookRide.this, response.toString(), Toast.LENGTH_LONG).show();

                        //Create the list of avaiable cars
                        for (i in 0 until response.length()) {
                            try {
                                // recupero l'oggetto
                                val resp = response[i] as JSONObject
                                val gson = GsonBuilder()
                                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                    .create()
                                val foundCar = gson.fromJson(
                                    resp.toString(),
                                    Car::class.java
                                ) // deserializes json into target2
                                availableCars.add(foundCar)
                                availableCarsSet.add(foundCar)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                        Log.println(Log.INFO, "Available Cars: ", availableCars.toString())

                        // Now create the instance of the CustomViewAdapter and pass
                        // the context and arrayList created above
                        val tempList = ArrayList<Car>()
                        tempList.addAll(availableCarsSet)
                        //TODO UNCOMMENT
                        // val customViewAdapter =CustomCarAdapter(this@BookRide, tempList, currentUser)
                        // set the CustomViewAdapter for ListView
                        // AvailableCarsListView?.setAdapter(customViewAdapter)
                    }

                    override fun onError(anError: ANError) {
                        // Qua cosa   fare se la richiesta va in errore
                        Toast.makeText(this@BookRide, anError.errorDetail, Toast.LENGTH_LONG).show()
                        println(anError)
                    }
                })
        })

        //TODO UNCOMMENT
           /* userAvatar?.setOnClickListener(View.OnClickListener { view: View ->
                val intent = Intent(view.context, UserDetailsActivity::class.java)
                //intent.putExtra("token", token);
                intent.putExtra("currentUser", currentUser)
                view.context.startActivity(intent)
            }) */
        /*
        BookRideButton.setOnClickListener(view -> {

            Intent intent = new Intent(view.getContext(), BookActivity.class);
            // Quindi posso passare il token di autenticazione all'altra activity
            //intent.putExtra("token", token);
            intent.putExtra("currentUser",currentUser);
            view.getContext().startActivity(intent);

        });*/
    }

    private fun updateLabelEndDate() {
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        DateChooseEditText!!.setText(dateFormat.format(myCalendar.time).replace("/", "-"))
    }

    private fun updateLabelEndTime() {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.US)
        TimeChooseEditText!!.setText(dateFormat.format(myCalendar.time))
    }
}