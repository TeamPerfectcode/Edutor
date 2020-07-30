package com.example.edutor

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Selection
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_edit_profile.*
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.HashMap

class EditProfileActivity : AppCompatActivity() {

    lateinit var gender_category : Spinner
    private var gen = arrayOfNulls<String>(4)
    private val fBase = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

//        supportActionBar!!.title = "Edit Profile"
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        supportActionBar!!.setDisplayShowHomeEnabled(true)

        gender_category = findViewById(R.id.spinner_gender)
        gen = resources.getStringArray(R.array.gender_of_user)

        val arr_adap = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, gen)
        arr_adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        gender_category.adapter = arr_adap

        gender_category.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

        }

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        et_enter_birthday_of_user.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, i, i2, i3 ->
                et_enter_birthday_of_user.setText(""+ i3 +"/"+ (i2+1) +"/"+ i)
            }, year, month, day)
            dpd.show()
        }

        val et_name = findViewById<EditText>(R.id.et_enter_name_of_user)
        val et_email_id = findViewById<EditText>(R.id.et_enter_email_id_of_user)
        val et_mob_no = findViewById<EditText>(R.id.et_enter_mobile_number_of_user)

        fBase.child("User").child(FirebaseAuth.getInstance().currentUser!!.uid).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(ds: DataSnapshot) {
                if (ds.exists()){
                    et_name.setText(ds.child("username").value.toString())
                    et_email_id.setText(ds.child("email").value.toString())
                    et_enter_mobile_number_of_user.setText(ds.child("phno").value.toString())
                    et_enter_birthday_of_user.setText(ds.child("dob").value.toString())
                }
                Selection.setSelection(et_name.text, et_name.text.length)
                Selection.setSelection(et_email_id.text, et_email_id.text.length)
                Selection.setSelection(et_mob_no.text, et_mob_no.text.length)
            }

        })

        save_btn.setOnClickListener {
            update_details_to_db()
        }

    }

    private fun update_details_to_db() {
        val name_of_user = et_enter_name_of_user.text.toString().trim()
        val email_id_of_user = et_enter_email_id_of_user.text.toString().trim()
        val gender_of_user = spinner_gender.selectedItem.toString().trim()
        val mob_no_of_user = et_enter_mobile_number_of_user.text.toString().trim()
        val dob_of_user = et_enter_birthday_of_user.text.toString().trim()

        if (TextUtils.isEmpty(name_of_user)){
            et_enter_name_of_user.error = "Name should not be empty!!"
        }
        else if (TextUtils.isEmpty(email_id_of_user)){
            et_enter_email_id_of_user.error = "Name should not be empty!!"
        }
        else if (gender_of_user == "Select Gender"){
            val s_g = spinner_gender.selectedView as TextView
            s_g.error = "Gender should not be empty!!"
        }
        else if (TextUtils.isEmpty(mob_no_of_user)){
            et_enter_mobile_number_of_user.error = "Mobile Number should not be empty!!"
        }
        else if(mob_no_of_user.length < 10){
            et_enter_mobile_number_of_user.error = "Invalid Format"
        }
        else if (TextUtils.isEmpty(dob_of_user)){
            et_enter_birthday_of_user.error = "Name should not be empty!!"
        }
        else{
            try {
                val userUpdatedDetails = HashMap<String, Any>()
                userUpdatedDetails["username"] = name_of_user
                userUpdatedDetails["email"] = email_id_of_user
                userUpdatedDetails["gender"] = gender_of_user
                userUpdatedDetails["phno"] = mob_no_of_user
                userUpdatedDetails["dob"] = dob_of_user

                fBase.child("User").child(FirebaseAuth.getInstance().currentUser!!.uid).updateChildren(userUpdatedDetails)
                    .addOnCompleteListener {
                        Toast.makeText(applicationContext, "Details Updated Successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(applicationContext, "Failed to update details", Toast.LENGTH_SHORT).show()
                    }


            }catch (e: Exception){
                Log.d("error", e.message.toString().trim())
            }
        }

    }

//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
//        return true
//    }
}