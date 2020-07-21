package com.example.edutor

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.edutor.home.Home
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        btsignup.setOnClickListener { startActivity(Intent(this, SignUp::class.java)) }
        btlogin.setOnClickListener {
            dbLogin()
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun dbLogin() {
//        if (!Patterns.EMAIL_ADDRESS.matcher(email.toString()).matches()) {
//            email.error = " Invalid email"
//            email.requestFocus()
//            return
//        }
//        if (password.toString().isEmpty()) {
//            password.error = "password cannot be empty"
//            password.requestFocus()
//            return
//        }
        prglogin.visibility=View.VISIBLE
        auth.signInWithEmailAndPassword(lemail.text.toString(), lpass.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //   Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    prglogin.visibility=View.GONE
                    updateUI(user)
                    Toast.makeText(
                        baseContext, "Login successfull.",
                        Toast.LENGTH_SHORT
                    ).show()


                } else {
                    prglogin.visibility=View.GONE

                    // If sign in fails, display a message to the user.
                    //  Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Login failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // ...
            }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this, Home::class.java))
            finish()
        }/*else{
            Toast.makeText(baseContext,"Login Failed",
                Toast.LENGTH_SHORT).show()
        }*/

    }
}