package com.example.madproject.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.madproject.R
import com.example.madproject.activities.MainActivity.Companion.UID
import com.example.madproject.activities.MainActivity.Companion.USER_ID
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RemoveAccount : AppCompatActivity() {
    private lateinit var RemoveBtn : Button
    private lateinit var CancelBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_account)

        val id =  intent.getStringExtra("REFID")
        Log.d("TAG", "this is ref id$id")

        RemoveBtn = findViewById(R.id.rmvBtn)
        CancelBtn =findViewById(R.id.cnslBtn)

        RemoveBtn.setOnClickListener{
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("data/${id}")

            myRef.removeValue().addOnSuccessListener {
               Log.d("TAG", "DELETED successfully")
            }.addOnFailureListener {
                it.message?.let { it1 -> Log.d("TAG", it1) }
            }

            val mAuth = FirebaseAuth.getInstance()
            mAuth.signOut()

            Toast.makeText(applicationContext,"Logged out", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        CancelBtn.setOnClickListener{
            val name = "intent.getStringExtra(MainActivity.EXTRA_NAME)"

            val mode = intent.getStringExtra("USER")

            val intent = Intent(applicationContext, Dashboard::class.java)

            if(mode == "GOOGLE_USER"){
                intent.putExtra("METHOD", 1001)
                intent.putExtra(USER_ID, MainActivity.USID)
            }else{

                // Check if user is logged in
                val sharedPreferences = getSharedPreferences("userSession", Context.MODE_PRIVATE)
                    val userName = sharedPreferences.getString("userName", "")

                    intent.putExtra("EXTRA_NAME",userName)
                    intent.putExtra("METHOD", 1002)
                    intent.putExtra(UID, USER_ID)
            }
            finish()
            startActivity(intent)
        }

        Log.d("TAG", "THIS IS REF FROM REMOVE $id")
    }
}