package com.example.edutor.home

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.bumptech.glide.Glide
import com.example.edutor.Login
import com.example.edutor.R
import com.example.edutor.adapter.PagerAdapter
import com.example.edutor.adapter.ZoomOutPageTransformer
import com.example.edutor.create.create_class
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mikhaellopez.circularimageview.CircularImageView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.navheader.*


class Home : AppCompatActivity() {

    lateinit var toogle: ActionBarDrawerToggle
    lateinit var adapter: PagerAdapter
    var prevMenuItem: MenuItem? = null
    lateinit var db: DatabaseReference
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
//        toogle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
//        drawer.addDrawerListener(toogle)
//        toogle.syncState()
        setdrawer(null)
        db = FirebaseDatabase.getInstance().reference
        sharedPreferences = getSharedPreferences(packageName, 0)
        adapter = PagerAdapter(supportFragmentManager)
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.explore -> pager.currentItem = 0
                R.id.mycourses -> pager.currentItem = 1
                R.id.profile -> pager.currentItem = 2
            }
            false
        }
        pager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (prevMenuItem != null) {
                    prevMenuItem!!.setChecked(false)
                } else {
                    bottom_navigation.menu.getItem(0).setChecked(false)
                }
                bottom_navigation.menu.getItem(position).isChecked = true
                prevMenuItem = bottom_navigation.menu.getItem(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
        setupPager()

    }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()
        updateheaderdetails()
        loaduserdata()
    }

    private fun updateheaderdetails() {
        if (sharedPreferences.contains("email")) {
            if (sharedPreferences.getString("email", "") == FirebaseAuth.getInstance().currentUser?.email.toString()) {
                val header = nav.getHeaderView(0)
                header.findViewById<TextView>(R.id.navusername).text = sharedPreferences.getString("username", "").toString()
                header.findViewById<TextView>(R.id.navuseremail).text = sharedPreferences.getString("email", "").toString()
                val proimg = header.findViewById<CircleImageView>(R.id.profileimg)
                Glide.with(this).load(sharedPreferences.getString("imageurl", "").toString()).into(proimg)
            }
        }
    }

    fun setdrawer(toolbar: Toolbar?) {
        toogle = ActionBarDrawerToggle(this, drawer,toolbar, R.string.open, R.string.close)
        drawer.addDrawerListener(toogle)
        toogle.syncState()
        nav.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.create -> {
                    startActivity(Intent(applicationContext,create_class::class.java))
                    finish()
                }
                R.id.message -> {
                }
                R.id.bookmark -> {
                }
                R.id.reminder -> {
                }
                R.id.files -> {
                }
                R.id.settings -> {
                }
                R.id.about -> {
                }
                R.id.logout -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(applicationContext, Login::class.java))
                    finish()
                }
            }
            drawer.closeDrawer(GravityCompat.START)
            true
        }
    }

    fun opendrawer() {
        drawer.openDrawer(GravityCompat.START)
    }

    fun setupPager() {
        val maincontent = MainContent()
        val currcourses = CurrentCourses()
        val account = MyAccount()
        adapter.addFrag(maincontent)
        adapter.addFrag(currcourses)
        adapter.addFrag(account)
        pager.adapter = adapter
        pager.setPageTransformer(
            true,
            ZoomOutPageTransformer()
        )
    }

    fun loaduserdata() {
        db.child("User").child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val email = snapshot.child("email").value.toString()
                            val name = snapshot.child("username").value.toString()
                            val phno = snapshot.child("phno").value.toString()
                            val proimg = snapshot.child("imageurl").value.toString()
                            sharedPreferences.edit().apply {
                                putString("email", email)
                                putString("username", name)
                                putString("phno", phno)
                                putString("imageurl", proimg)
                            }.apply()
                            navuseremail.text = email
                            navusername.text = name
                            Glide.with(this@Home).load(proimg).into(profileimg)
                        }
                    }
                }
            )
    }
}