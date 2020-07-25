package com.example.edutor.create

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.edutor.R
import com.example.edutor.home.Home
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_class.*
import java.util.*
import kotlin.collections.HashMap

class create_class : AppCompatActivity() {
    lateinit var db: DatabaseReference
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_class)
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference
        val title:TextInputEditText = findViewById(R.id.titleEdit)
        val desc:TextInputEditText = findViewById(R.id.descEdit)
        val toggleState:SwitchMaterial=findViewById(R.id.toggler)
        var access=""
        toggleState.textOn="1"
        toggleState.textOff="0"
        create_button.setOnClickListener {
            val class_id = UUID.randomUUID().toString()
            val classTitle = title.text.toString()
            val classDesc = desc.text.toString()
            if(toggleState.isChecked) {
                access = toggleState.textOn.toString()
            }
            else
                access=toggleState.textOff.toString()
            var hMap: HashMap<String, Any> = HashMap<String, Any>()
            hMap.put("id",class_id)
            hMap.put("title",classTitle)
            hMap.put("description",classDesc)
            hMap.put("access",access)
            Log.d("d1","$classTitle $classDesc")
            db.child("COURSES").child(class_id).setValue(hMap).addOnSuccessListener {
                Toast.makeText(
                    baseContext, "Class created successfully",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this,Home::class.java))
                finish()
            }
        }
    }
}