package com.example.madproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.madproject.R
import com.google.firebase.database.FirebaseDatabase
class UpdateVacancy : AppCompatActivity() {

    private lateinit var title : TextView
    private lateinit var desc : TextView
    private lateinit var update : Button
    private lateinit var cancel : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_vacancy)

        initView()
        setValuesToViews()

        update.setOnClickListener{
            val name = title.text.toString()
            val description = desc.text.toString()
            var count =0

            if (name.isEmpty()){
                title.error = "This field must ne entered"
                count =+ 1
            }
            if (description.isEmpty()){
                desc.error = "This field must ne entered"
                count =+ 1
            }

            if (count==0){

                val dbRef = FirebaseDatabase.getInstance().getReference("post")
                val ref = dbRef.child(intent.getStringExtra("PostID")!!)
                ref.child("companyName").setValue(name)
                ref.child("description").setValue(description)

                Toast.makeText(this,"Updated Successfully", Toast.LENGTH_LONG).show()
                val intent = Intent(this@UpdateVacancy, CurrentVc::class.java)
                finish()
                startActivity(intent)
            }


        }
        cancel.setOnClickListener{
            val intent = Intent(this@UpdateVacancy, CurrentVc::class.java)
            finish()
            startActivity(intent)
        }

    }

    private fun initView() {
        title = findViewById(R.id.titleName)
        desc = findViewById(R.id.descBox)
        update = findViewById(R.id.updateBtn)
        cancel = findViewById(R.id.cancelRBtn)
    }

    private fun setValuesToViews(){
        title.text = intent.getStringExtra("companyName")
        desc.text = intent.getStringExtra("description")
    }

}