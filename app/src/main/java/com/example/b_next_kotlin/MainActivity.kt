package com.example.b_next_kotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.b_next_kotlin.databinding.ActivityMainBinding
import com.google.gson.GsonBuilder
import model.User
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    var signInText: TextView? = null
    var infoText: TextView? = null
    var infoText2: TextView? = null
    var infoText3: TextView? = null
    var emailText: TextView? = null
    var passwordText: TextView? = null
    var inputEmail: EditText? = null
    var inputPassword: EditText? = null
    var loginButton: Button? = null
    var facebookButton: Button? = null
    var signUpButton: Button? = null
    var currentUser: User? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        // assign values to each control of the layout
        signInText = findViewById(R.id.signInText)
        infoText = findViewById(R.id.infoText)
        //infoText2 = findViewById(R.id.infoText2);
        infoText3 = findViewById(R.id.infoText3)
        emailText = findViewById(R.id.emailText)
        passwordText = findViewById(R.id.passwordText)
        inputEmail = findViewById(R.id.inputEmail)
        inputPassword = findViewById(R.id.inputPassword)
        loginButton = findViewById(R.id.loginButton)
        //facebookButton = findViewById(R.id.facebookButton);
        signUpButton = findViewById(R.id.signUpButton)

        // Populate the UI with Fast Android Networking Library
        AndroidNetworking.initialize(applicationContext)
        loginButton?.setOnClickListener(View.OnClickListener { view: View? ->
            if (inputEmail?.length() != 0 && inputPassword?.length() != 0) {
                val user = arrayOf(
                    User(
                        inputEmail?.getText().toString(),
                        inputPassword?.getText().toString()
                    )
                )
                AndroidNetworking.initialize(applicationContext)
                AndroidNetworking.post(url + "user/signin") //negli header per il token fare sempre così .addHeaders("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9")
                    .addApplicationJsonBody(user[0])
                    .setPriority(Priority.LOW)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject) {
                            Log.println(Log.INFO, "signIn result", "Inizio")


                            // Qua cosa   fare se la richiesta funziona
                            try {
                                token = response["token"] as String
                                val gson = GsonBuilder() //.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                    //.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
                                    .create()
                                currentUser = gson.fromJson(
                                    response["user"].toString(),
                                    User::class.java
                                ) // deserializes json into target2
                                //System.out.println("####################################\n"+currentUser.toString());
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                            Log.println(Log.INFO, "SignIn result", "Success")
                            //og.println(Log.INFO,"User: ", user.toString());

                            // il login ha avuto successo, vado alla prossima pagina
                            val intent = Intent(this@MainActivity, BookRide::class.java)

                            // Quindi posso passare il token di autenticazione all'altra activity
                            intent.putExtra("currentUser", currentUser)
                            //intent.putExtra("token",  token);

                            //Create the bundle
                            /*Bundle b = new Bundle();
                            //Add your data to bundle
                            b.putString("token", token);
                            intent.putExtras(b);*/startActivity(intent)
                        }

                        override fun onError(error: ANError) {
                            // Qua cosa   fare se la richiesta va in errore
                            Toast.makeText(
                                this@MainActivity,
                                "Something went wrong with Log In",
                                Toast.LENGTH_SHORT
                            ).show()
                            println(error)
                        }
                    })
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "You must enter username and password to login",
                    Toast.LENGTH_SHORT
                ).show()
                Log.println(
                    Log.INFO,
                    "signIn result",
                    "You must enter username and password to login"
                )
            }
        })
        signUpButton?.setOnClickListener(View.OnClickListener { view: View? ->
            val intent = Intent(this@MainActivity, SignUp::class.java)
            startActivity(intent)
        })

    }


    companion object {
        // Token ottenuto dal login
        var token = ""
        var url = "http://10.0.2.2:8080/"
    }



}