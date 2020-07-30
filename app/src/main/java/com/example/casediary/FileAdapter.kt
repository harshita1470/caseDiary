package com.example.casediary

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView


class FileAdapter(val context: Context,val itemList:ArrayList<FileStructure>):RecyclerView.Adapter<FileAdapter.FileViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.hearing_single_row,parent,false)
        return FileAdapter.FileViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val item=itemList[position]
        holder.data.text=item.file
        holder.data.setOnClickListener {
            val intent=Intent(Intent.ACTION_VIEW)
            println("intent created")
            println(item.file)
            intent.setDataAndType(Uri.parse(item.file),item.type)
            val chooser=Intent.createChooser(intent,"Open with...")
            println("about to start")
            context.startActivity(chooser)
        }

    }

    class FileViewHolder(view: View): RecyclerView.ViewHolder(view){
        val data:TextView=view.findViewById(R.id.date)
    }
}