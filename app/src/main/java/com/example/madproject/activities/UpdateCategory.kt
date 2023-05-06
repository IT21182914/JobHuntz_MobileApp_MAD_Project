package com.example.madproject.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.madproject.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateCategory: AppCompatActivity() {
   //declare the components
    private lateinit var updateBtn: Button
    private lateinit var cancelBtn : Button
    private lateinit var updateField : TextView
    private lateinit var dbRef : DatabaseReference

    //override method on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update)

        //initialize the components
        updateBtn = findViewById(R.id.updateCategoy)
        cancelBtn = findViewById(R.id.cancelUBrn)
        updateField = findViewById(R.id.ucategoryName)



        updateField.text = intent.getStringExtra("categoryName").toString()

        //set on clickner to update button
        updateBtn.setOnClickListener{
            if (updateField.text.isEmpty()){
                updateField.error = "This field must be entered"
            }else{
                updateField.error = null
                updateCategory()
            }
        }
        //set on clicklistner to cancel button
        cancelBtn.setOnClickListener{
            val intent = Intent(this@UpdateCategory, CategoryMain::class.java)
            startActivity(intent)
            finish()
        }
    }
//create update category method
    private fun updateCategory() {
        val name = updateField.text.toString()
        val id = intent.getStringExtra("categoryID")
        Log.d("TAG", "$id")

        dbRef = FirebaseDatabase.getInstance().getReference("category")

        val ref = dbRef.child(id!!)
        Log.d("TAG", "This is $ref")
        ref.child("categoryName").setValue(name)

        Toast.makeText(this,"Updated Successfully", Toast.LENGTH_LONG).show()
        val intent = Intent(this@UpdateCategory, CategoryMain::class.java)
        startActivity(intent)
        finish()

    }
}
