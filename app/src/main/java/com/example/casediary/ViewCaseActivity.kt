package com.example.casediary

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.text.SimpleDateFormat
import java.util.*

class ViewCaseActivity : AppCompatActivity() {
    lateinit var caseName:TextView
    lateinit var caseYear:TextView
    lateinit var caseP:TextView
    lateinit var caseD:TextView
    lateinit var caseT:TextView
    lateinit var caseDesc:TextView
    lateinit var next:Button
    lateinit var recyclerview:RecyclerView
    lateinit var layoutmanager: RecyclerView.LayoutManager
    val hearingList= arrayListOf<String>(
//        "22 dec", "25 aug","7 sept"
    )
    lateinit var recycleradapter: HearingRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_case)

        caseName=findViewById(R.id.caseName)
        caseYear=findViewById(R.id.caseYear)
        caseD=findViewById(R.id.caseD)
        caseP=findViewById(R.id.caseP)
        caseT=findViewById(R.id.caseT)
        caseDesc=findViewById(R.id.caseDesc)
        next=findViewById(R.id.next)
        recyclerview=findViewById(R.id.recyclerView)

        var caseid:String?=""
        caseid=intent.getStringExtra("id")
        val realm= Realm.getDefaultInstance()
        updateDates(caseid)

        layoutmanager=LinearLayoutManager(this)
        recycleradapter= HearingRecyclerAdapter(this,hearingList,caseid)

        supportActionBar?.title="Case Details"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val cal= Calendar.getInstance()

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "dd/MM/yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
              var case=realm.where<CaseStructure>().equalTo("id",caseid).findFirst()!!
               realm.executeTransaction{
                    val hearing=realm.createObject<HearingStructure>()
                    hearing.date=sdf.format(cal.getTime())
                    case.hearings.add(hearing)
                }
//               Toast.makeText(this@ViewCaseActivity,"done",Toast.LENGTH_SHORT).show()
                updateDates(intent.getStringExtra("id"))
                recycleradapter.notifyDataSetChanged()

            }
        }

        if(intent!= null) {
            val case=realm.where<CaseStructure>().equalTo("id",intent.getStringExtra("id")).findFirst()!!
            caseName.text=case.name
            caseYear.text=case.year
            caseP.text=case.plantiff
            caseD.text=case.defendant
            caseT.text=case.type
            caseDesc.text=case.desc
            realm.close()
        }
        else {
            finish()
            Toast.makeText(this,"No such case exists !",Toast.LENGTH_SHORT).show()
        }

        next.setOnClickListener{

            DatePickerDialog(this,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        recyclerview.layoutManager=layoutmanager
        recyclerview.adapter=recycleradapter

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)

    }
    fun updateDates(stringExtra: String?) {
        hearingList.clear()
        val realm=Realm.getDefaultInstance()
        val list=realm.where<CaseStructure>().equalTo("id",stringExtra).findFirst()!!
        for(hearing in list.hearings) {
            hearingList.add(hearing.date)
        }

    }
}
