package com.example.madproject.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.madproject.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DeleteCategory : AppCompatActivity(){
    //declare the components
    private lateinit var deleteBtn : Button
    private lateinit var cancelBtn : Button
    private lateinit var dbRef : DatabaseReference

    //override oncrete method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.delete_category)
//inizialize the components
        deleteBtn = findViewById(R.id.deleteDBtn)
        cancelBtn = findViewById(R.id.canceldBtn)
        //set on clicklistner to delete btn
        deleteBtn.setOnClickListener{
            val id = intent.getStringExtra("categoryID").toString()
            dbRef = FirebaseDatabase.getInstance().getReference("category")
            val ref = dbRef.child(id).removeValue()
                .addOnCompleteListener{
                    Toast.makeText(this, "Post deleted successfully", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@DeleteCategory, CategoryMain::class.java)
                    startActivity(intent)
                }.addOnFailureListener{err ->
                    Log.e("err", "error : ${err.message}")
                }
        }
        //set on click listner to cancel btn
        cancelBtn.setOnClickListener{
            val intent = Intent(this@DeleteCategory, CategoryMain::class.java)
            finish()
            startActivity(intent)
        }

    }
}
