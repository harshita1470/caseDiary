package com.example.casediary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import io.realm.Case
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where

class NewCaseActivity : AppCompatActivity() {
    lateinit var caseName:EditText
    lateinit var caseYear:EditText
    lateinit var caseP:EditText
    lateinit var caseD:EditText
    lateinit var caseT:EditText
    lateinit var caseDesc:EditText
    lateinit var submit:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_case)
        val realm= Realm.getDefaultInstance()

        caseName=findViewById(R.id.caseName)
        caseYear=findViewById(R.id.caseYear)
        caseP=findViewById(R.id.caseP)
        caseD=findViewById(R.id.caseD)
        caseT=findViewById(R.id.caseT)
        caseDesc=findViewById(R.id.caseDesc)
        submit=findViewById(R.id.submit)


        supportActionBar?.title="New Case"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        submit.setOnClickListener {

            val name=caseName.text.toString().trim()
            val year=caseYear.text.toString().trim()
            val plantiff=caseP.text.toString().trim()
            val defendant=caseD.text.toString().trim()
            val type=caseT.text.toString().trim()
            val desc=caseDesc.text.toString().trim()


            realm.executeTransaction{
                val case=realm.createObject<CaseStructure>()
                println("data to be added")
                case.id=realm.where<CaseStructure>().findAll().size.toString()
                case.name=name
                case.year=year
                case.plantiff=plantiff
                case.defendant=defendant
                case.type=type
                case.desc=desc

            }
            Toast.makeText(this,"Your Case is Saved !",Toast.LENGTH_SHORT).show()
            caseName.text.clear()
            caseYear.text.clear()
            caseP.text.clear()
            caseD.text.clear()
            caseT.text.clear()
            caseDesc.text.clear()

            realm.close()
            onBackPressed()
        }



    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)

    }
}
