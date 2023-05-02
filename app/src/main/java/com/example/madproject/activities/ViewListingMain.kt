package com.example.madproject.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.madproject.R



class ViewListingMain : AppCompatActivity() {

    private lateinit var btnJobAdd : Button
    private lateinit var btnJobView : Button



    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_listing_main)


            btnJobAdd = findViewById(R.id.btnJobAdd)

              btnJobAdd.setOnClickListener{

                  val intent = Intent(this, JobListForm::class.java)
                  startActivity(intent)

              }

        btnJobView = findViewById(R.id.btnJobView)

        btnJobView.setOnClickListener{

            val intent = Intent(this, FetchingActivity::class.java)
            startActivity(intent)

        }

    }
}