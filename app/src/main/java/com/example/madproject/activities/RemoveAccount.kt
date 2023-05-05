package com.example.madproject.activities


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.madproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RemoveAccount : AppCompatActivity() {
    private lateinit var removeBtn : Button
    private lateinit var cancelBtn : Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_account)


        //getting details from shared preference
        val sp = getSharedPreferences("userSession", Context.MODE_PRIVATE)

        val userNid = sp.getString("userId", "")
        val userName = sp.getString("userName", "")
        val userImage = sp.getString("userImage", "")
        val userPhone = sp.getString("userPhoto", "")
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
        Log.d("TAG", "this is ref id$userNid")

        removeBtn = findViewById(R.id.rmvBtn)
        cancelBtn =findViewById(R.id.cnslBtn)

        removeBtn.setOnClickListener{

            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("data/$userNid")
            Log.d("TAG", "USER REMOVE ID : $userNid")

            myRef.removeValue().addOnSuccessListener {
               Log.d("TAG", "DELETED successfully")

                val mAuth = FirebaseAuth.getInstance()
                mAuth.signOut()

                Toast.makeText(applicationContext,"Logged out", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()

            }.addOnFailureListener {
                it.message?.let { it1 -> Log.d("TAG", it1) }
            }

        }

        cancelBtn.setOnClickListener{
            val intent = Intent(applicationContext, Dashboard::class.java)
            startActivity(intent)
            finish()
        }

        Log.d("TAG", "THIS IS REF FROM REMOVE $userNid")
    }
}