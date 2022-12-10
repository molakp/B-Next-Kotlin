package com.example.b_next_kotlin

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import com.google.gson.Gson
import model.Car
import model.Position
import model.User
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class BookActivity : AppCompatActivity() {
    var nameOfCar: TextView? = null
    var priceText: TextView? = null
    var oreText: TextView? = null
    var textReservation: TextView? = null
    var startReservation: TextView? = null
    var endReservation: TextView? = null
    var hourStart: EditText? = null
    var dateStart: EditText? = null
    var hourEnd: EditText? = null
    var dateEnd: EditText? = null
    var bookButton: Button? = null
    var carImageReservation: ImageView? = null
    val myCalendar: Calendar = Calendar.getInstance()
    var myFormat = "yyyy/MM/dd"
    var currentCar: Car? = null
    var currentUser: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)
        currentCar = intent.getSerializableExtra("currentCar") as Car?
        currentUser = intent.getSerializableExtra("currentUser") as User?
        /*
        currentCar= new Car(UUID.fromString("19658da5-f573-4c7d-931d-15cd80dc676d"));
        currentUser = new User(UUID.fromString("67f65c6a-4627-4449-b4f5-beb14f0c4139"));*/nameOfCar =
            findViewById(R.id.titlePayament)
        priceText = findViewById(R.id.priceText)
        oreText = findViewById(R.id.oreText)
        textReservation = findViewById(R.id.resumePayament)
        startReservation = findViewById(R.id.startBooking)
        endReservation = findViewById(R.id.endBooking)
        dateStart = findViewById(R.id.dateStartPayment)
        hourStart = findViewById(R.id.hourStartPayment)
        dateEnd = findViewById(R.id.dateEndPayment)
        hourEnd = findViewById(R.id.hourEndPayment)
        bookButton = findViewById(R.id.bookButton)
        carImageReservation = findViewById(R.id.carImageReservation)
        supportActionBar!!.setTitle("Reservation")
        val src =
            (currentCar!!.name?.lowercase(Locale.getDefault()) ?: "") + "_" + currentCar!!.carModel?.lowercase(
                Locale.getDefault()
            )
        val drawableId = this.resources.getIdentifier(src, "drawable", this.packageName)
        carImageReservation?.setImageResource(drawableId)
        AndroidNetworking.initialize(applicationContext)
        nameOfCar!!.setText(currentCar?.name  +" "+ currentCar?.carModel)
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //nameOfCar.setTypeface(null,Typeface.BOLD);
        //textReservation.setTypeface(Typeface.DEFAULT_BOLD);
        oreText?.setText(currentCar?.priceHour.toString())
        //priceText.setText("Price / Hour: ");
        //priceText.append(currentCar.getPriceHour().toString()+ " €");
        val start_date = OnDateSetListener { view: DatePicker?, year: Int, month: Int, day: Int ->
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = month
            myCalendar[Calendar.DAY_OF_MONTH] = day
            updateLabelStartDate()
        }
        val start_hour = OnTimeSetListener { view: TimePicker?, hour: Int, minute: Int ->
            myCalendar[Calendar.HOUR_OF_DAY] = hour
            myCalendar[Calendar.MINUTE] = minute
            updateLabelStartTime()
        }
        val end_date = OnDateSetListener { view: DatePicker?, year: Int, month: Int, day: Int ->
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = month
            myCalendar[Calendar.DAY_OF_MONTH] = day
            updateLabelEndDate()
        }
        val end_hour = OnTimeSetListener { view: TimePicker?, hour: Int, minute: Int ->
            myCalendar[Calendar.HOUR_OF_DAY] = hour
            myCalendar[Calendar.MINUTE] = minute
            updateLabelEndTime()
        }
        //Toast.makeText(this, myCalendar.get(Calendar.HOUR_OF_DAY), Toast.LENGTH_SHORT).show();
        hourStart?.setOnClickListener(View.OnClickListener { view: View? ->
            TimePickerDialog(
                this, start_hour,
                myCalendar[Calendar.HOUR_OF_DAY], myCalendar[Calendar.MINUTE], true
            ).show()
        })
        dateStart?.setOnClickListener(View.OnClickListener { view: View? ->
            DatePickerDialog(
                this,
                start_date,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        })
        hourEnd?.setOnClickListener(View.OnClickListener { view: View? ->
            TimePickerDialog(
                this, end_hour,
                myCalendar[Calendar.HOUR_OF_DAY], myCalendar[Calendar.MINUTE], true
            ).show()
        })
        dateEnd?.setOnClickListener(View.OnClickListener { view: View? ->
            DatePickerDialog(
                this,
                end_date,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        })


        //editText.setOnClickListener(this::showDatePickerDialog);
        bookButton?.setOnClickListener(View.OnClickListener { view: View ->
            handlingErrors()
            // verifico che i campi non siano nulli in quanto servono sia una data di inizio che di fine prenotazione
            if (TextUtils.isEmpty(dateStart?.getText())) {
                dateStart?.setError("The item cannot be empty")

            } else if (TextUtils.isEmpty(hourStart?.getText())) {
                hourStart?.setError("The item cannot be empty")

            } else if (TextUtils.isEmpty(dateEnd?.getText())) {
                dateEnd?.setError("The item cannot be empty")

            } else if (TextUtils.isEmpty(hourEnd?.getText())) {
                hourEnd?.setError("The item cannot be empty")

            } else {
                val jsonObject = JSONObject()
                val startOfBook =
                    dateStart?.getText().toString().replace("/", "-") + "T" + hourStart?.getText()
                        .toString() + ":00"
                val endOfBook =
                    dateEnd?.getText().toString().replace("/", "-") + "T" + hourEnd?.getText()
                        .toString() + ":00"
                println("Inizio prenotazone ############## $startOfBook")

                //Toast.makeText(BookActivity.this, isAviable(String.valueOf(currentCar.getCarId()),startOfBook,endOfBook)? "ritorna TRUE" : "ritorna FALSE", Toast.LENGTH_SHORT).show();


                // verifico che le date siano corrette, che quindi la fine di prenotazione sia una data
                // successiva a quella di inizio prenotazione
                if (isAfter(endOfBook, startOfBook)) {
                    // Faccio una chiamata al back-end per vedere se la macchina che ha selezionato l'utente è disponibile
                    try {
                        jsonObject.put("startOfBook", startOfBook)
                        jsonObject.put("endOfBook", endOfBook)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
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
                                var isAviable = false
                                var i = 0
                                while (i < response.length() && !isAviable) {
                                    try {
                                        // recupero l'oggetto
                                        val resp = response[i] as JSONObject
                                        // se gli ID tra risposta alle macchine disponibili, e la macchina corrente corrispondono,
                                        // allora setto a true isAviable
                                        //System.out.println("Sto confrontando "+ currentCar.getCarId()+ " " + "con "+ resp.get("carId"));
                                        if (currentCar!!.carId.toString() == resp["carId"]) {
                                            isAviable = true
                                            println("E disponibileee!!! " + resp["carId"])
                                        } else println("NON è disponibileee!!! " + resp["carId"])
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                    i++
                                }

                                // se la macchina è disponibile posso aggiungere la prenotazione
                                if (isAviable) {
                                    makeReservation(startOfBook, endOfBook)
                                } else Toast.makeText(
                                    view.context,
                                    "Macchina non disponibile",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun onError(error: ANError) {
                                // handle error
                                // Qua cosa   fare se la richiesta va in errore
                                Toast.makeText(
                                    this@BookActivity,
                                    error.errorCode,
                                    Toast.LENGTH_LONG
                                ).show()
                                println(error)
                            }
                        })


                    //Log.d("Errore", "Macchina non disponibile");
                } else Toast.makeText(
                    this@BookActivity,
                    "La data di inizio prenotazione deve essere precedente a quella di fine prenotazione!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun updateLabelStartDate() {
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        dateStart!!.setText(dateFormat.format(myCalendar.time))
    }

    private fun updateLabelStartTime() {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.US)
        hourStart!!.setText(dateFormat.format(myCalendar.time))
    }

    private fun updateLabelEndDate() {
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        dateEnd!!.setText(dateFormat.format(myCalendar.time))
    }

    private fun updateLabelEndTime() {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.US)
        hourEnd!!.setText(dateFormat.format(myCalendar.time))
    }

    private fun handlingErrors() {
        dateStart!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (dateStart!!.text.toString() === "") {
                    dateStart!!.error = "Enter start Date"
                } else {
                    dateStart!!.error = null
                }
            }
        })
        dateEnd!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (dateEnd!!.text.toString() === "") {
                    dateEnd!!.error = "Enter end Date"
                } else {
                    dateEnd!!.error = null
                }
            }
        })
        hourStart!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (hourStart!!.text.toString() === "") {
                    hourStart!!.error = "Enter start Hour"
                } else {
                    hourStart!!.error = null
                }
            }
        })
        hourEnd!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (hourEnd!!.text.toString() === "") {
                    hourEnd!!.error = "Enter end Hour"
                } else {
                    hourEnd!!.error = null
                }
            }
        })
    }

    private fun makeReservation(startOfBook: String, endOfBook: String) {
        val jsonObject = JSONObject()
        val rangeMin = -180
        val rangeMax = 180
        // genero dei numeri casuali per la posizione
        val startPosition = Position(
            ThreadLocalRandom.current().nextDouble(rangeMin.toDouble(), rangeMax.toDouble()),
            ThreadLocalRandom.current().nextDouble(rangeMin.toDouble(), rangeMax.toDouble())
        )
        val destination = Position(
            ThreadLocalRandom.current().nextDouble(rangeMin.toDouble(), rangeMax.toDouble()),
            ThreadLocalRandom.current().nextDouble(rangeMin.toDouble(), rangeMax.toDouble())
        )
        try {
            jsonObject.put("startOfBook", startOfBook)
            jsonObject.put("endOfBook", endOfBook)
            jsonObject.put("user", JSONObject(Gson().toJson(currentUser)))
            jsonObject.put("car", JSONObject(Gson().toJson(currentCar)))
            jsonObject.put("destination", JSONObject(Gson().toJson(startPosition)))
            jsonObject.put("startPosition", JSONObject(Gson().toJson(destination)))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        AndroidNetworking.post(MainActivity.Companion.url + "reservations/add") //negli header per il token fare sempre così .addHeaders("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9")
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
                    //Log.d("Reservation", jsonObject.toString());
                    //Toast.makeText(BookActivity.this, "Reservation added SUCESFULLY!", Toast.LENGTH_SHORT).show();
                    Log.d("OK!!", "Reservation added SUCESFULLY!")

                    // La prenotazione è stata aggiunta con successo, posso passare quindi alla prossima activity
                    val intent = Intent(this@BookActivity, PaymentActivity::class.java)
                    // Quindi posso passare il token di autenticazione all'altra activity
                    //intent.putExtra("token", token);
                    intent.putExtra("currentUser", currentUser)
                    intent.putExtra("currentCar", currentCar)
                    intent.putExtra("startOfBook", startOfBook)
                    intent.putExtra("endOfBook", endOfBook)
                    this@BookActivity.startActivity(intent)
                }

                override fun onError(anError: ANError) {
                    // Qua cosa   fare se la richiesta va in errore
                    Toast.makeText(this@BookActivity, anError.errorDetail, Toast.LENGTH_LONG).show()
                    println(anError)
                }
            })
    }

    private fun isAfter(endOfBook: String, startOfBook: String): Boolean {
        val format: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        var startDate: Date? = null
        var endDate: Date? = null
        try {
            startDate = format.parse(startOfBook)
            endDate = format.parse(endOfBook)
        } catch (e: ParseException) {
            Log.e("Error parsing date", "$startOfBook $endOfBook")
            e.printStackTrace()
        }

        // la data di fine prenotazione è successiva alla data di inizio prenotazione?
        return endDate!!.compareTo(startDate) > 0
    }
}