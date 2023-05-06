package com.example.madproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.madproject.R
import com.google.firebase.database.FirebaseDatabase

//declaring components
class UpdateVacancy : AppCompatActivity() {

    private lateinit var title : TextView
    private lateinit var desc : TextView
    private lateinit var update : Button
    private lateinit var cancel : Button


    //initialize components
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_vacancy)

        initView()
        setValuesToViews()

//set on click listener to update button
        update.setOnClickListener{
            //get data from text fields
            val name = title.text.toString()
            val description = desc.text.toString()

            //count variable to check whether the fields are empty
            var count =0

            if (name.isEmpty()){
                title.error = "This field must ne entered"
                count =+ 1
            }
            if (description.isEmpty()){
                desc.error = "This field must ne entered"
                count =+ 1
            }
            //count variable to check whether the fields are empty
            if (count==0){

                val dbRef = FirebaseDatabase.getInstance().getReference("post")
                val ref = dbRef.child(intent.getStringExtra("PostID")!!)
                ref.child("companyName").setValue(name)
                ref.child("description").setValue(description)

                //toast message to show the user that the data is updated

                Toast.makeText(this,"Updated Successfully", Toast.LENGTH_LONG).show()
                val intent = Intent(this@UpdateVacancy, CurrentVc::class.java)
                finish()
                startActivity(intent)
            }


        }
        //set on click listener to cancel button
        cancel.setOnClickListener{
            val intent = Intent(this@UpdateVacancy, CurrentVc::class.java)
            finish()
            startActivity(intent)
        }

    }



    //creating function to view
    private fun initView() {
        title = findViewById(R.id.titleName)
        desc = findViewById(R.id.descBox)
        update = findViewById(R.id.updateBtn)
        cancel = findViewById(R.id.cancelRBtn)
    }


    //creating function to view
    private fun setValuesToViews(){
        title.text = intent.getStringExtra("companyName")
        desc.text = intent.getStringExtra("description")
    }

}

