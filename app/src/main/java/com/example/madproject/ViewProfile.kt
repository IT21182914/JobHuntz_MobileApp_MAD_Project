package com.example.madproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class ViewProfile : AppCompatActivity() {

    private lateinit var editBtn : Button
    private lateinit var deleteBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)

        editBtn = findViewById(R.id.editDetails)
        deleteBtn = findViewById(R.id.deleteDetails)


        editBtn.setOnClickListener{

            val intent = Intent(applicationContext, EditProfileDetails::class.java)


            Log.d("TAG", "User id is in view profile : ${MainActivity.USER_ID} ")

            startActivity(intent)
        }



    }
}