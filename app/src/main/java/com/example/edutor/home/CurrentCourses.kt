package com.example.edutor.home

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.edutor.R

class CurrentCourses : Fragment() {
    lateinit var layoutview: View
    lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutview = inflater.inflate(R.layout.fragment_current_courses, container, false)
        toolbar = layoutview.findViewById<Toolbar>(R.id.toolbar_course)
        toolbar.title = "Courses"
        (context as Home).setSupportActionBar(toolbar)
        (context as Home).setdrawer(toolbar)
        setHasOptionsMenu(true)
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


}