package com.example.edutor

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.edutor.home.Home
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {
    lateinit var db: DatabaseReference
    private lateinit var auth: FirebaseAuth
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()
        sharedPreferences=getSharedPreferences(packageName,0)
        db = FirebaseDatabase.getInstance().reference
        btSubmit.setOnClickListener {
            signupUser()
        }
    }

    private fun signupUser() {
        val inputusername: TextInputEditText = findViewById(R.id.usn)
        val inputemail: TextInputEditText = findViewById(R.id.usemail)
        val inputpass: TextInputEditText = findViewById(R.id.uspass)
        val inputphno: TextInputEditText = findViewById(R.id.usphon)
        val susername = inputusername.text.toString().trim()
        val semail = inputemail.text.toString().trim()
        val spass = inputpass.text.toString().trim()
        val phno = inputphno.text.toString().trim()
        Log.d("data", "$semail $spass")
        if (susername.isNotEmpty() and semail.isNotEmpty() and phno.isNotEmpty()) {
            prgsignup.visibility = View.VISIBLE
            auth.createUserWithEmailAndPassword(semail, spass)
                .addOnSuccessListener {

                    val hashMap = hashMapOf(
                        "username" to susername,
                        "email" to semail,
                        "phno" to phno
                    )
                    Log.d("datatt", it.user!!.uid.toString())
                    db.child("User")
                        .child(it.user!!.uid.toString())
                        .setValue(hashMap)
                        .addOnSuccessListener {
                            prgsignup.visibility = View.GONE
                            Toast.makeText(
                                baseContext, "SignUp successful.",
                                Toast.LENGTH_SHORT
                            ).show()
                            sharedPreferences.edit().putString("username",susername).apply()
                            sharedPreferences.edit().putString("email",semail).apply()
                            sharedPreferences.edit().putString("phno",semail).apply()
                            startActivity(Intent(this, Home::class.java))
                            finish()
                        }.addOnFailureListener {
                            prgsignup.visibility = View.GONE
                            Log.d("exc", it.message.toString())
                        }

                }
                .addOnFailureListener {
                    prgsignup.visibility = View.GONE
                    Toast.makeText(
                        baseContext, "SignUp failed,try again later",
                        Toast.LENGTH_SHORT
                    ).show()

                }

        }
    }
}