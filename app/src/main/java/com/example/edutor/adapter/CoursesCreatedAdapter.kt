package com.example.edutor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.edutor.R
import com.example.edutor.model.CoursesCreated

class CoursesCreatedAdapter(var context : Context, var list : ArrayList<CoursesCreated>) : RecyclerView.Adapter<coursesCreatedHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): coursesCreatedHolder {
        return coursesCreatedHolder(LayoutInflater.from(context).inflate(R.layout.my_courses_card_layout_recycler, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun update(list: ArrayList<CoursesCreated>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: coursesCreatedHolder, position: Int) {
        val title = holder.itemView.findViewById<TextView>(R.id.title_of_the_subject)
        val desc = holder.itemView.findViewById<TextView>(R.id.description_of_the_subject)
        val no_of_stud = holder.itemView.findViewById<TextView>(R.id.tv_no_of_students)

        title.text = list[position].title_of_subject
        desc.text = list[position].description_of_subject
        //no_of_stud.text = list[position].no_of_students
    }

}

class coursesCreatedHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
}
