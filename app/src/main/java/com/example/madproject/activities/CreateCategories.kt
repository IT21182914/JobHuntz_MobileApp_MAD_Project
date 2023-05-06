package com.example.madproject.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.madproject.R
import com.example.madproject.models.CategoryModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateCategories: AppCompatActivity() {
    //Declaration the components
    private lateinit var cancel: Button
    private lateinit var addBtn : Button
    private lateinit var categoryField : EditText
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create)
//inizialize the components
        cancel = findViewById(R.id.cancelBtn)
        addBtn = findViewById(R.id.addBtn)
        categoryField = findViewById(R.id.categoryFeild)

        //set on click listner to cancel
        cancel.setOnClickListener{
            val intent = Intent(this@CreateCategories, CategoryMain::class.java)
            finish()
            startActivity(intent)
        }
        //set on click listner to add button
        addBtn.setOnClickListener{
            validateAndPush()
        }
    }
//validate and push method
    private fun validateAndPush() {
        val cateName = categoryField.text.toString()

        if (cateName.isEmpty()){
            categoryField.error = "This field must be entered"
        }else{
            //add a firebase connection
            dbRef = FirebaseDatabase.getInstance().getReference("category")
            val categoryId = dbRef.push().key!!

            val category = CategoryModel(categoryId, cateName)

            dbRef.child(categoryId).setValue(category)
                    //when the category added successfully
                .addOnCompleteListener{
                    categoryField.text.clear()
                    Toast.makeText(this, "Category added successfully",Toast.LENGTH_LONG).show()
                }.addOnFailureListener{err ->
                    //when the category added wrong it shows error
                    Log.e("Error", "${err.message}")
                }

        }

    }

}
