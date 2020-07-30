package com.example.casediary

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HearingRecyclerAdapter(val context:Context, val itemList:ArrayList<String>,val itemCase:String):RecyclerView.Adapter<HearingRecyclerAdapter.HearingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HearingViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.hearing_single_row,parent,false)
        return HearingViewHolder(view)
    }

    override fun getItemCount(): Int {
       return itemList.size
    }

    override fun onBindViewHolder(holder: HearingViewHolder, position: Int) {
        val item=itemList[position]
            holder.date.text=item
            val case=itemCase
        holder.date.setOnClickListener {
            val intent= Intent(context,HearingActivity::class.java)
            intent.putExtra("date",item)
            intent.putExtra("id",case)
            context.startActivity(intent)
        }
    }

    class HearingViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val date:TextView=view.findViewById(R.id.date)
    }
}