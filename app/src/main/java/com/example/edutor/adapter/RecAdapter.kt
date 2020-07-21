package com.example.edutor.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.edutor.R
import com.example.edutor.model.active
import com.google.android.material.progressindicator.ProgressIndicator
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList

class RecAdapter(var context: Context, var list: ArrayList<active>, var pos:Int) :
    RecyclerView.Adapter<RecHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecHolder {
        var view:View
        if(viewType==1) {
             view = LayoutInflater.from(context).inflate(R.layout.activecourse, parent, false)
        }
        else{
            view= LayoutInflater.from(context).inflate(R.layout.trendrec, parent, false)
        }
        return RecHolder(view)
    }
    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: RecHolder, position: Int) {
        if(pos==1) {
            val logo = holder.itemView.findViewById<CircleImageView>(R.id.courselogo)
            val c1 = holder.itemView.findViewById<TextView>(R.id.text1)
            val c2 = holder.itemView.findViewById<TextView>(R.id.text2)
            val p = holder.itemView.findViewById<ProgressIndicator>(R.id.ind)
            logo.setImageResource(list[position].imgurl)
            c1.text = list[position].coursename1
            c2.text = list[position].coursename2
            p.progress = list[position].progress
        }
        else{
            val c1 = holder.itemView.findViewById<TextView>(R.id.text1)
            val c2 = holder.itemView.findViewById<TextView>(R.id.text2)
            val tcard = holder.itemView.findViewById<CardView>(R.id.tcard)
            val trel = holder.itemView.findViewById<RelativeLayout>(R.id.trel)
            val color=getRandomColor()
            tcard.setCardBackgroundColor(color)
            trel.setBackgroundColor(color)
            c1.text = list[position].coursename1
            c2.text = list[position].coursename2
        }
    }
    override fun getItemViewType(position: Int): Int {
        return if(pos==1)
            1
        else
            2
    }
    fun getRandomColor(): Int {
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }
}
class RecHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
}