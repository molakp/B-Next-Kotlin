package com.example.b_next_kotlin

import android.annotation.SuppressLint
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
import com.google.android.material.textfield.TextInputEditText
import model.User

class SignUp : AppCompatActivity() {
    var name: EditText? = null
    var surname: EditText? = null
    var username: EditText? = null
    var password: EditText? = null
    var buttonSignUp: Button? = null
    var alredyRegitered: Button? = null
    var birthdate: TextInputEditText? = null
    var currentUser: User? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        name = findViewById(R.id.name)
        surname = findViewById(R.id.surname)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        birthdate = findViewById(R.id.birthdate)
        buttonSignUp = findViewById(R.id.buttonSignUp)
        alredyRegitered = findViewById(R.id.alredyRegitered)

        // Populate the UI with Fast Android Networking Library
        AndroidNetworking.initialize(applicationContext)
        buttonSignUp?.setOnClickListener(View.OnClickListener { view: View ->
            Toast.makeText(this, "messaggio di prova", Toast.LENGTH_SHORT)
            /*if (isEmpty(name.getText())) {
                Toast t = Toast.makeText(this, "You must enter first name to register!", Toast.LENGTH_SHORT);
                t.show();
            }
            if (isEmpty(surname.getText())) {
                Toast t = Toast.makeText(this, "You must enter surname to register!", Toast.LENGTH_SHORT);
                t.show();
            }
            if (isEmpty(username.getText())) {
                Toast t = Toast.makeText(this, "You must enter username to register!", Toast.LENGTH_SHORT);
                t.show();
            }
            if (isEmpty(password.getText())) {
                Toast t = Toast.makeText(this, "You must enter password to register!", Toast.LENGTH_SHORT);
                t.show();
            }
            if (isEmpty(birthdate.getText())) {
                Toast t = Toast.makeText(this, "You must enter birthdate to register!", Toast.LENGTH_SHORT);
                t.show();
            }*/if (name?.length() != 0 && surname?.length() != 0 && username?.length() != 0 && password?.length() != 0) {
            //Date date = (Date)birthdate.getText();
            //DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            //String strDate = dateFormat.format(date);
            //2020-12-31T10:00:00.000+00:00
            val user = User(
                name?.getText().toString(),
                surname?.getText().toString(),
                birthdate?.getText().toString(),
                username?.getText().toString(),
                password?.getText().toString()
            )
            //User user = new User(inputEmail2.getText().toString(), inputPassword2.getText().toString());
            AndroidNetworking.post(MainActivity.Companion.url + "user/signup") //negli header per il token fare sempre così .addHeaders("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9")
                .addApplicationJsonBody(user)
                .setPriority(Priority.LOW)
                .build()
                .getAsString(object : StringRequestListener {
                    override fun onResponse(response: String) {
                        Log.println(
                            Log.INFO,
                            "signUp result",
                            "Registrazione effettuata con successo!"
                        )
                        Log.println(Log.INFO, "Request is: ", user.toString())
                        //og.println(Log.INFO,"User: ", user.toString());

                        // il login ha avuto successo, vado alla prossima pagina
                        val intent = Intent(view.context, MainActivity::class.java)
                        startActivity(intent)
                    }

                    override fun onError(error: ANError) {
                        // Qua cosa   fare se la richiesta va in errore
                        Toast.makeText(
                            view.context,
                            "Something went wrong with Sign Up",
                            Toast.LENGTH_SHORT
                        ).show()
                        println(error)
                    }
                })
        } else {
            Toast.makeText(
                this@SignUp,
                "You must enter username and password to login",
                Toast.LENGTH_SHORT
            ).show()
            Log.println(Log.INFO, "signIn result", "You must enter username and password to login")
        }
        })
        alredyRegitered?.setOnClickListener(View.OnClickListener { view: View ->
            val intent = Intent(view.context, MainActivity::class.java)
            intent.putExtra("currentUser", currentUser)
            startActivity(intent)
        })
    }
}