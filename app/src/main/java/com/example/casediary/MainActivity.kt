package com.example.casediary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where

class MainActivity : AppCompatActivity() {

    lateinit var newCase:Button
    lateinit var recyclerView:RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager


    val caselist= arrayListOf<Case>()
    lateinit var recyclerAdapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("testdb.realm")
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)

        updateData()

        newCase=findViewById(R.id.newCase)
        recyclerView=findViewById(R.id.recycler_view)
        layoutManager= LinearLayoutManager(this)
        recyclerAdapter= RecyclerAdapter(this,caselist)


        supportActionBar?.title="Your Cases"
        newCase.setOnClickListener{
            val intent=Intent(this,NewCaseActivity::class.java)
            startActivity(intent)
        }

        recyclerView.layoutManager=layoutManager
        recyclerView.adapter=recyclerAdapter



    }

    override fun onResume() {
        super.onResume()
        println("onResume called")
        caselist.clear()
        updateData()
        recyclerAdapter.notifyDataSetChanged()
    }

    fun updateData() {
        println("data recycled")
        val realm=Realm.getDefaultInstance()

        val cases=realm.where<CaseStructure>().findAll()
        realm.executeTransaction{
            for(case in cases) {
                val caseObject= Case (
                    case.id,
                    case.name,
                    case.year,
                    case.plantiff,
                    case.defendant,
                    case.type,
                    case.desc
                )
                caselist.add(caseObject)
            }


        }

    }
}
