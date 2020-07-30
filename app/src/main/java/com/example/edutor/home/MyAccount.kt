package com.example.edutor.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.edutor.EditProfileActivity
import com.example.edutor.R
import com.example.edutor.adapter.CoursesCreatedAdapter
import com.example.edutor.adapter.RecAdapter
import com.example.edutor.model.CoursesCreated
import com.example.edutor.model.active
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_my_account.*
import kotlinx.android.synthetic.main.fragment_my_account.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class MyAccount : Fragment() {

    private val fBase = FirebaseDatabase.getInstance().reference
    private var selectedPhotoUri : Uri? = null
    private var coms : Bitmap? = null
    lateinit var byteArrayOutputStream : ByteArrayOutputStream
    lateinit var imgPath : UploadTask
    lateinit var imgData : ByteArray
    lateinit var fStorage : FirebaseStorage
    private var storageReference : StorageReference? = null
    lateinit var recyclerView : RecyclerView
    lateinit var coursesCreatedAdapter: CoursesCreatedAdapter
    private val mArrayList : ArrayList<CoursesCreated> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_account, container, false)

        fStorage = FirebaseStorage.getInstance()
        storageReference = fStorage.reference

        val btn_edit_profile = view.findViewById<MaterialButton>(R.id.edit_profile)
        btn_edit_profile.setOnClickListener {
            startActivity(Intent(activity, EditProfileActivity::class.java))
        }

        val name = view.findViewById<TextView>(R.id.name_of_user)
        val email = view.findViewById<TextView>(R.id.email_id_of_user)

        fBase.child("User").child(FirebaseAuth.getInstance().currentUser!!.uid).addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(ds: DataSnapshot) {
                if (ds.exists()){
                    name.text = ds.child("username").value.toString()
                    email.text = ds.child("email").value.toString()
                    Glide.with(this@MyAccount).load(ds.child("imageurl").value.toString())
                        .placeholder(R.drawable.unisex_avatar).dontAnimate().fitCenter().into(profile_pic_of_user)
                }
            }
        })

        view.profile_pic_of_user.setOnClickListener {
            val permission = ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE)
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (permission != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                    //ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                }
                else {
                    showFileChooser()
                }
            }
            else{
                showFileChooser()
            }
        }

        recyclerView = view.findViewById(R.id.my_created_course_recycler_view)
        recyclerView.setHasFixedSize(true)
        val linLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linLayoutManager
        coursesCreatedAdapter = CoursesCreatedAdapter(context!!, mArrayList)
        recyclerView.adapter = coursesCreatedAdapter

        fBase.child("COURSES")
            //.child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("rbi7h0ROkIR31YwVqBhkiTixfKE2")
            .addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                mArrayList.clear()
                if (snapshot.exists()){
                    for (ds in snapshot.children){
                        try {
                            val types = CoursesCreated(
                                ds.child("title").value.toString(),
                                ds.child("description").value.toString()
                            )
                            mArrayList.add(types)
                        }catch (e: Exception){
                            e.printStackTrace()
                        }
                    }
                    coursesCreatedAdapter.update(mArrayList)
                }
            }

        })

        val btm_nav_view = activity!!.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && btm_nav_view.isShown){
                    btm_nav_view.visibility = View.GONE
                }
                else if(dy < 0){
                    btm_nav_view.visibility = View.VISIBLE
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        return view
    }

    private fun showFileChooser(){
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1).start(context!!, this)
    }

    private fun uploadFile(){
        if (selectedPhotoUri != null){
            val newFile = File(selectedPhotoUri!!.path)
            try {
                coms = Compressor(context).setMaxWidth(125)
                    .setMaxHeight(125)
                    .setQuality(50)
                    .compressToBitmap(newFile)
            }catch(e : IOException){
                e.printStackTrace()
            }
            byteArrayOutputStream = ByteArrayOutputStream()
            coms?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            imgData = byteArrayOutputStream.toByteArray()
            imgPath = storageReference!!.child("UsersProfileImage").child(FirebaseAuth.getInstance().currentUser!!.uid).putBytes(imgData)
            imgPath.addOnCompleteListener {
                if (it.isSuccessful){
                    storeData(it as UploadTask)
                }else{
                    Toast.makeText(view!!.context, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun storeData(uploadTask: UploadTask) {
        uploadTask.result.storage.downloadUrl.addOnSuccessListener {uri->
            val userData = HashMap<String, Any>()
            userData["imageurl"] = uri.toString()

            val ref = fBase.child("User").child(FirebaseAuth.getInstance().currentUser!!.uid)
            ref.updateChildren(userData)
                .addOnCompleteListener {
                    Toast.makeText(activity!!.applicationContext, "Profile Pic Added Successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(activity!!.applicationContext, "Failed to add details", Toast.LENGTH_SHORT).show()
                }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            val result: CropImage.ActivityResult = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK){
                selectedPhotoUri = result.uri
                try {
                    val b = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, selectedPhotoUri)
                    Glide.with(this).load(b).into(profile_pic_of_user)
                    uploadFile()
                }catch (e: IOException){
                    e.printStackTrace()
                }
            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                val e = result.error
                Log.d("er", e.toString())
            }
        }
    }

}