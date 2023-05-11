package com.example.madproject.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.madproject.R


class ViewListingMain : AppCompatActivity() {


    //buttons initialization
    private lateinit var btnJobAdd : Button
    private lateinit var btnJobView : Button



    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_listing_main)

        //getting details from shared preference
        val sp = getSharedPreferences("userSession", Context.MODE_PRIVATE)

        val userNid = sp.getString("userId", "")
        val userName = sp.getString("userName", "")
        val userImage = sp.getString("userImage", "")
        val userPhone = sp.getString("userPhone", "")
        val userBio = sp.getString("userBio", "")
        val userEmail = sp.getString("userEmail", "")
        val isLogIn = sp.getBoolean("isLoggedIn", false)
        val isAdmin = sp.getBoolean("isAdmin", false)

        val loggedInUser = sp.getString("loggedInUser", "")
        val userRefID = sp.getString("refId", "")
        val googleUserId = sp.getString("googleUserID", "")


        Log.d(
            "TAGD", "Log activity " +
                    "\n$userNid and " +
                    "\n$userName $userImage" +
                    "\n$userPhone" +
                    "\n$userBio" +
                    "\n$userEmail"
        )

        Log.d(
            "TAGX", "This is admins status: $isAdmin"

        )
        if(isAdmin){
            btnJobAdd.visibility = View.VISIBLE
            Log.d(
                "TAGX", "This is admins true"

            )

        }else{
            btnJobAdd.visibility = View.GONE
            Log.d(
                "TAGX", "This is adminsfalse"

            )
        }


            //set path to the btnJobAdd buttons
            btnJobAdd = findViewById(R.id.btnJobAdd)

              btnJobAdd.setOnClickListener{

                  val intent = Intent(this, JobListForm::class.java)
                  startActivity(intent)

              }


            //set path to the btnJobView buttons
            btnJobView = findViewById(R.id.btnJobView)

              btnJobView.setOnClickListener{

                  val intent = Intent(this, FetchingActivity::class.java)
                  startActivity(intent)

        }

    }
}