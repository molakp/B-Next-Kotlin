package com.example.b_next_kotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import model.Car
import model.User
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class PaymentActivity : AppCompatActivity() {
    var titlePayament: TextView? = null
    var resumePayament: TextView? = null
    var startBooking: TextView? = null
    var endBooking: TextView? = null
    var dateStartPayment: TextView? = null
    var dateEndPayment: TextView? = null
    var hourStartPayment: TextView? = null
    var hourEndPayment: TextView? = null
    var totalText: TextView? = null
    var feedbackButton: Button? = null
    var successImage: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        val currentCar = intent.getSerializableExtra("currentCar") as Car?
        val currentUser = intent.getSerializableExtra("currentUser") as User?
        val startOfBook = intent.getSerializableExtra("startOfBook") as String?
        val endOfBook = intent.getSerializableExtra("endOfBook") as String?
        titlePayament = findViewById(R.id.titlePayament)
        successImage = findViewById(R.id.successImage)
        resumePayament = findViewById(R.id.resumePayament)
        startBooking = findViewById(R.id.startBooking)
        endBooking = findViewById(R.id.endBooking)
        dateStartPayment = findViewById(R.id.dateStartPayment)
        dateEndPayment = findViewById(R.id.dateEndPayment)
        hourStartPayment = findViewById(R.id.hourStartPayment)
        hourEndPayment = findViewById(R.id.hourEndPayment)
        totalText = findViewById(R.id.totalText)
        feedbackButton = findViewById(R.id.feedbackButton)
        val dateStart = startOfBook!!.split("T").toTypedArray()[0]
        // substring la uso per eliminare i secondi e il separatore :
        val hourStart = startOfBook.split("T").toTypedArray()[1].substring(
            0,
            startOfBook.split("T").toTypedArray()[1].length - 3
        )
        val dateEnd = endOfBook!!.split("T").toTypedArray()[0]
        // substring la uso per eliminare i secondi e il separatore :
        val hourEnd = endOfBook.split("T").toTypedArray()[1].substring(
            0,
            endOfBook.split("T").toTypedArray()[1].length - 3
        )

        // setto opportunamente le date e gli orari
        dateStartPayment?.setText(dateStart)
        hourStartPayment?.setText(hourStart)
        dateEndPayment?.setText(dateEnd)
        hourEndPayment?.setText(hourEnd)
        totalText?.append(
            (currentCar?.priceHour!! * getNumberOfHours(
                startOfBook,
                endOfBook
            )).toString() + "€"
        )

        //currentCar.getReservation()
        //totalText.setText(currentCar.getReservation().listIterator().next().getStartOfBook().toString());


        // vado all'activity dei feedback
        feedbackButton?.setOnClickListener(View.OnClickListener { view: View ->
            /* Per ora va direttamente,
            dopo va fatto che si procede a quella dopo
            selezionando una macchina e passando all'activity
            seguente le informazioni
            * */
            val intent = Intent(view.context, AddComment::class.java)
            // Quindi posso passare il token di autenticazione all'altra activity
            //intent.putExtra("token", token);
            intent.putExtra("currentUser", currentUser)
            intent.putExtra("currentCar", currentCar)
            view.context.startActivity(intent)
        })
    }

    private fun getNumberOfHours(endOfBook: String?, startOfBook: String?): Long {
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
        assert(endDate != null)
        val numberOfHours = (startDate!!.time - endDate!!.time) / (60 * 60 * 1000)
        Log.d(
            "Payment",
            "S " + startDate.time + " E " + endDate.time + "Number of hours: " + numberOfHours
        )
        return numberOfHours
    }
}