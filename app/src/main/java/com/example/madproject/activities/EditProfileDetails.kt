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
import com.example.madproject.activities.MainActivity.Companion.METHOD
import com.example.madproject.activities.MainActivity.Companion.USER_ID
import com.example.madproject.activities.MainActivity.Companion.USID
import com.example.madproject.models.GoogleUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


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


        edtName = findViewById(R.id.userEditName)
        edtEmail = findViewById(R.id.userEditMail)
        edtPhone = findViewById(R.id.userEditPhone)
        edtBio = findViewById(R.id.userEdtBio)
        updateBtn = findViewById(R.id.saveChangesBtn)
        cancelbtn = findViewById(R.id.cancelChanges)




        if (intent.getStringExtra("USER") == "GOOGLE_USER") {
            Log.d("TAG", "GOOGLEUSER IS HERE")

            edtName.text = intent.getStringExtra("NAME")
            edtBio.text = intent.getStringExtra("BIO")

            edtPhone.visibility = View.GONE
            edtEmail.visibility = View.GONE

            updateBtn.setOnClickListener{
                updateDetailsGu()
            }

        }else{
            edtName.text = intent.getStringExtra("NAME")
            edtEmail.text = intent.getStringExtra("EMAIL")
            edtPhone.text = intent.getStringExtra("PHONE")
            edtBio.text = intent.getStringExtra("BIO")

            updateBtn.setOnClickListener{
                updateDetails()
            }
        }


        cancelbtn.setOnClickListener{
            val mode = intent.getStringExtra("USER")
            val intent = Intent(applicationContext, Dashboard::class.java)

            if(mode == "GOOGLE_USER"){
                intent.putExtra("METHOD", 1001)
                intent.putExtra(USER_ID, USID)
            }else{

                // Check if user is logged in
                val sharedPreferences = getSharedPreferences("userSession", Context.MODE_PRIVATE)
                val userName = sharedPreferences.getString("userName", "")

                intent.putExtra("EXTRA_NAME",userName)
                intent.putExtra("METHOD", 1002)
                intent.putExtra(MainActivity.UID, USER_ID)
            }

            startActivity(intent)
            finish()
        }
    }
    private fun updateDetails() {
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
            val ref = dbRef.getReference("data").child(MainActivity.USER_ID)

            ref.child("name").setValue(name)
            ref.child("email").setValue(email)
            ref.child("phone").setValue(phone)
            ref.child("bio").setValue(bio)

            val intent = Intent(applicationContext, ViewProfile::class.java)
                intent.putExtra("METHOD", 1002)
            startActivity(intent)
        }

    }

    private fun updateDetailsGu() {
        name = edtName.text.toString()
        bio = edtBio.text.toString()

        val dbRef = FirebaseDatabase.getInstance()
        val ref = dbRef.getReference("data")

        ref.orderByChild("userId").equalTo(USID)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    //if user exist in firebase db
                    if (dataSnapshot.exists()) {


                        // Data exists, retrieve the value
                        for (Dat in dataSnapshot.children) {
                            val userData = Dat.getValue(GoogleUser::class.java)


                            var count = 0

                            if (name.isEmpty()){
                                edtName.error = "This field must be entered"
                                count += 1
                            }
                            if (count == 0){
                                val dbRefs = FirebaseDatabase.getInstance()
                                val refs = dbRefs.getReference("data").child(userData?.refIId.toString())

                                refs.child("name").setValue(name)
                                refs.child("bio").setValue(bio)

                                val intent = Intent(applicationContext, ViewProfile::class.java)
                                intent.putExtra(METHOD, 1001)
                                startActivity(intent)
                                finish()
                            }


                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })




    }
}