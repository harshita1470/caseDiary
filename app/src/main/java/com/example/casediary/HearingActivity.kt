package com.example.casediary

import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.edit_hearing.view.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Environment
import android.widget.ImageView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.getExternalFilesDirs
import androidx.core.content.FileProvider
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class HearingActivity : AppCompatActivity() {
    lateinit var date:TextView
    lateinit var order:TextView
    lateinit var applicno:TextView
    lateinit var listedon:TextView
    lateinit var edit:Button
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager:RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FileAdapter
    lateinit var add:Button
    lateinit var currentPath:String
    lateinit var image:ImageView
    var fileList= arrayListOf<FileStructure>(
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hearing)
        val realm=Realm.getDefaultInstance()
        date=findViewById(R.id.date)
        order=findViewById(R.id.order)
        applicno=findViewById(R.id.applicno)
        listedon=findViewById(R.id.listedon)
        edit=findViewById(R.id.edit)
        recyclerView=findViewById(R.id.recyclerView)
        add=findViewById(R.id.add)
        image=findViewById(R.id.image)
        layoutManager=LinearLayoutManager(this)
        recyclerAdapter= FileAdapter(this,fileList)

        supportActionBar?.title="Hearing Details"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var case=realm.where<CaseStructure>().equalTo("id",intent.getStringExtra("id")).findFirst()!!
        var hearing=case.hearings.where().equalTo("date",intent.getStringExtra("date")).findFirst()!!
        realm.executeTransaction{

            applicno.text=hearing.applicno
            date.text=hearing.date
            order.text=hearing.order
            listedon.text=hearing.listedon

            for(file in hearing.files) {
                fileList.add(file)
            }
        }

        add.setOnClickListener {
            dispatchTakePictureIntent()
        }

        edit.setOnClickListener {
              val dialog=LayoutInflater.from(this).inflate(R.layout.edit_hearing,null)
            val builder=AlertDialog.Builder(this).setView(dialog).setTitle("Edit Info")
            dialog.applicno.setText(hearing.applicno)
            dialog.date.setText(hearing.date)
            dialog.order.setText(hearing.order)
            dialog.listedon.setText(hearing.listedon)
            val alertDialog=builder.show()
            dialog.submit.setOnClickListener {
                alertDialog.dismiss()
                realm.executeTransaction{
                    hearing.date=dialog.date.text.toString()
                    hearing.applicno=dialog.applicno.text.toString()
                    hearing.order=dialog.order.text.toString()
                    hearing.listedon=dialog.listedon.text.toString()

                    applicno.text=hearing.applicno
                    date.text=hearing.date
                    order.text=hearing.order
                    listedon.text=hearing.listedon

                }
            }
            dialog.cancel.setOnClickListener {
                alertDialog.dismiss()

            }


        }

        recyclerView.layoutManager=layoutManager
        recyclerView.adapter=recyclerAdapter

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        galleryAddPic()
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val file=File(currentPath)
            val uri=Uri.fromFile(file)//The uri with the location of the file
            val realm=Realm.getDefaultInstance()
            var case=realm.where<CaseStructure>().equalTo("id",intent.getStringExtra("id")).findFirst()!!
            var hearing=case.hearings.where().equalTo("date",intent.getStringExtra("date")).findFirst()!!
            val mimeType: String? = uri?.let { returnUri ->
                contentResolver.getType(returnUri)
            }
            realm.executeTransaction{
                val files=realm.createObject<FileStructure>()
                files.file=uri.toString()
                files.type=mimeType
                hearing.files.add(files)
            }
             recyclerAdapter.notifyDataSetChanged()



        }
        Toast.makeText(this,"done",Toast.LENGTH_SHORT).show()
    }


    private fun galleryAddPic() {
        val file=File(currentPath)
        val b=BitmapFactory.decodeFile(currentPath)
        file.parentFile.mkdir()
        val fileOut=FileOutputStream(file)
          b.compress(Bitmap.CompressFormat.JPEG,100,fileOut)
        fileOut.flush()
        fileOut.close()
        sendBroadcast( Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(currentPath)))
        println("file saved")
        }


    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val capI=File(storageDir,"JPEG_${timeStamp}")
        capI.createNewFile()
        currentPath=capI.absolutePath
        println(currentPath)
        return capI
    }
    private fun dispatchTakePictureIntent() {
       val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        println("File not created")
        if(intent.resolveActivity(packageManager)!= null) {
            println("File to be created")
            var photoFile:File? =null
            try {
                photoFile=createImageFile()
            }catch (e:IOException) {
                Toast.makeText(this,"File not created",Toast.LENGTH_SHORT).show()
            }
            println("File created")
            if(photoFile!=null) {
                var photoUri=FileProvider.getUriForFile(this,"com.example.casediary.fileprovider",photoFile)
                println("found uri")
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
                println("camera opening")

                startActivityForResult(intent,1)
            }
        }

    }

}
