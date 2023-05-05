package com.example.madproject.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.madproject.R
import com.google.firebase.database.FirebaseDatabase


class EditProfileDetails : AppCompatActivity() {

    private lateinit var edtName : TextView
    private lateinit var edtEmail : TextView
    private lateinit var edtPhone : TextView
    private lateinit var edtBio : TextView
    private lateinit var updateBtn : Button
    private lateinit var cancelbtn : Button

    private lateinit var name: String
    private lateinit var email : String
    private lateinit var phone : String
    private lateinit var bio : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_details)

        //getting details from shared preference
        val sp = getSharedPreferences("userSession", Context.MODE_PRIVATE)

        val userNid = sp.getString("userId", "")
        val userName = sp.getString("userName", "")
        val userImage = sp.getString("userImage", "")
        val userPhone = sp.getString("userPhone", "")
        val userBio = sp.getString("userBio", "")
        val userEmail = sp.getString("userEmail", "")

        val isLogIn = sp.getBoolean("isLoggedIn", false)
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


        edtName = findViewById(R.id.userEditName)
        edtEmail = findViewById(R.id.userEditMail)
        edtPhone = findViewById(R.id.userEditPhone)
        edtBio = findViewById(R.id.userEdtBio)
        updateBtn = findViewById(R.id.saveChangesBtn)
        cancelbtn = findViewById(R.id.cancelChanges)



        //if user login google
        if (loggedInUser == "GOOGLE_USER") {

            edtName.text = userName
            edtBio.text = userBio

            edtPhone.visibility = View.GONE
            edtEmail.visibility = View.GONE

            updateBtn.setOnClickListener{
                if (userNid != null) {
                    updateDetailsGu(userNid)
                    Log.d("TAG", "USERID IS $userNid")
                }
            }

        }else if(loggedInUser == "FIREBASE_USER"){
            edtName.text = userName
            edtEmail.text = userEmail
            edtPhone.text = userPhone
            edtBio.text = userBio

            updateBtn.setOnClickListener{
                if (userNid != null) {
                    updateDetails(userNid)
                }
            }
        }


        cancelbtn.setOnClickListener{

            val intent = Intent(applicationContext, ViewProfile::class.java)
            startActivity(intent)
            finish()

        }
    }
    private fun updateDetails(userNid:String) {

        name = edtName.text.toString()
        email = edtEmail.text.toString()
        phone = edtPhone.text.toString()
        bio = edtBio.text.toString()

        var count = 0

        if (name.isEmpty()){
            edtName.error = "This field must be entered"
            count += 1
        }
        if (email.isEmpty()){
            edtEmail.error = "This field must be entered"
            count += 1
        }
        if (phone.isEmpty()){
            edtPhone.error = "This field must be entered"
            count += 1
        }
        if (count == 0){

            val dbRef = FirebaseDatabase.getInstance()
            val ref = dbRef.getReference("data").child(userNid)

            ref.child("name").setValue(name)
            ref.child("email").setValue(email)
            ref.child("phone").setValue(phone)
            ref.child("bio").setValue(bio)

            val intent = Intent(applicationContext, Dashboard::class.java)
            startActivity(intent)
        }

    }

    private fun updateDetailsGu(userNid:String) {
        name = edtName.text.toString()
        bio = edtBio.text.toString()

        var count = 0

        if (name.isEmpty()){
            edtName.error = "This field must be entered"
            count += 1
        }

        if (count == 0){

            val dbRef = FirebaseDatabase.getInstance()
            val ref = dbRef.getReference("data").child(userNid)

            ref.child("name").setValue(name)
            ref.child("bio").setValue(bio)

            val intent = Intent(applicationContext, Dashboard::class.java)
            startActivity(intent)
            finish()
        }



    }
}