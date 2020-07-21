package com.example.edutor.home

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edutor.R
import com.example.edutor.adapter.RecAdapter
import com.example.edutor.model.active

class MainContent : Fragment() {

    lateinit var toolbar: Toolbar
    lateinit var layoutview: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        layoutview = inflater.inflate(R.layout.fragment_main_content, container, false)
        toolbar = layoutview.findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = ""
        (context as Home).setSupportActionBar(toolbar)
        (context as Home).setdrawer(toolbar)
        setHasOptionsMenu(true)
        addtempdata()
        return layoutview
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
        val item3 = menu.add(0, 1234, Menu.NONE, "Seach")
        item3.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
        item3.setIcon(R.drawable.ic_baseline_search_24)
        var s = SpannableString(item3.title.toString())
        s.setSpan(ForegroundColorSpan(Color.WHITE), 0, s.length, 0)
        item3.title = s
        super.onPrepareOptionsMenu(menu)
    }

    fun addtempdata() {
        val currec = layoutview!!.findViewById<RecyclerView>(R.id.currec)
        val trendc = layoutview!!.findViewById<RecyclerView>(R.id.trendc)
        var list = ArrayList<active>()
        var activeobj = active()
        activeobj.imgurl = R.drawable.p1
        activeobj.coursename1 = "Business"
        activeobj.coursename2 = "Management"
        activeobj.progress = 20
        list.add(activeobj)
        activeobj = active()
        activeobj.imgurl = R.drawable.p3
        activeobj.coursename1 = "IT and "
        activeobj.coursename2 = "Cloud Computing"
        activeobj.progress = 50
        list.add(activeobj)
        activeobj = active()
        activeobj.imgurl = R.drawable.p4
        activeobj.coursename1 = "Basic of"
        activeobj.coursename2 = "Bakery"
        activeobj.progress = 40
        list.add(activeobj)
        activeobj = active()
        activeobj.imgurl = R.drawable.p5
        activeobj.coursename1 = "Learn"
        activeobj.coursename2 = "Guitar"
        activeobj.progress = 70
        list.add(activeobj)
        var adapter = RecAdapter(context!!, list, 1)
        currec.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        currec.adapter = adapter
        var list2 = ArrayList<active>()
        activeobj = active()
        activeobj.coursename1 = "Business Analysis"
        activeobj.coursename2 = "Learn Business Analysis from top educators"
        list2.add(activeobj)
        activeobj = active()
        activeobj.coursename1 = "Security in Advance ML "
        activeobj.coursename2 = "Learn advanced security algorithms used for handling ML systems "
        list2.add(activeobj)
        var adapter2 = RecAdapter(context!!, list2, 2)
        trendc.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        trendc.adapter = adapter2
    }
}