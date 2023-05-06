package com.example.madproject.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.madproject.R
import com.google.firebase.database.FirebaseDatabase

class DeletePost : AppCompatActivity() {
    //declaring components
    private lateinit var deletePBtn : Button
    private lateinit var cancelPBtn :Button


    //create function
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.delete)

        // initializing variables
        deletePBtn = findViewById(R.id.deletepBtn)
        cancelPBtn = findViewById(R.id.cancelpBtn)

        //set on click listener to delete button
        deletePBtn.setOnClickListener{
            val dbRef = FirebaseDatabase.getInstance().getReference("post")
            val ref = dbRef.child(intent.getStringExtra("PostID")!!)
            ref.removeValue().addOnCompleteListener{
                Toast.makeText(this,"Post deleted successfully",Toast.LENGTH_LONG).show()
                val intent = Intent(this@DeletePost, CurrentVc::class.java)
                startActivity(intent)

            }.addOnFailureListener{err ->
                Log.e("err", "Error ${err.message}")
            }
        }

        //set on click listener to cancel button
        cancelPBtn.setOnClickListener{
            val intent = Intent(this@DeletePost, CurrentVc::class.java)
            finish()
            startActivity(intent)
        }
    }
}