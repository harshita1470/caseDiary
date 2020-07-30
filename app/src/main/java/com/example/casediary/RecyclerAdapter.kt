package com.example.casediary

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(val context:Context, val itemList: ArrayList<Case>):RecyclerView.Adapter<RecyclerAdapter.FrontViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrontViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.single_row,parent,false)
        return  FrontViewHolder(view)
    }

    override fun getItemCount(): Int {
            return itemList.size
    }

    override fun onBindViewHolder(holder: FrontViewHolder, position: Int) {
        val case=itemList[position]
        holder.caseName.text=case.caseN
        holder.caseYear.text=case.caseY

        holder.llcontent.setOnClickListener {
            val intent= Intent(context,ViewCaseActivity::class.java)
            intent.putExtra("id",case.caseId)
            context.startActivity(intent)
        }
    }

    class  FrontViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val caseName:TextView=view.findViewById(R.id.caseName)
        val caseYear:TextView=view.findViewById(R.id.caseYear)
        val llcontent:LinearLayout=view.findViewById(R.id.llcontent)
    }
}