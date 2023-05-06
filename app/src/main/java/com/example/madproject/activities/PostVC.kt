package com.example.madproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.madproject.models.PostVacancyModel
import com.example.madproject.R
import com.google.firebase.database.FirebaseDatabase


//declaring components
class PostVC : AppCompatActivity() {
    //declaring components
    private lateinit var company : EditText
    private lateinit var description : EditText

    private lateinit var post : Button
    private lateinit var cancel : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_vc)

        //initialize components
        company = findViewById(R.id.titleName)
        description = findViewById(R.id.descField)

        post = findViewById(R.id.postBtn)
        cancel = findViewById(R.id.cancelBtn)


        //set on click listener to cancel button
        cancel.setOnClickListener{
            val intent = Intent(this, PostJob::class.java)
            finish()
            startActivity(intent)

        }
        //set on click listener to post button
        post.setOnClickListener{
            saveData()
        }




    }


    //creating function to save data
    private fun saveData() {
        val name = company.text.toString()
        val desc = description.text.toString()

        var count = 0

        if (name.isEmpty()){
            company.error = "This field mush be entered"
            count += 1
        }
        if (desc.isEmpty()){
            description.error = "This field must be entered"
            count += 1
        }
        if (count == 0){

            val dbRef = FirebaseDatabase.getInstance().getReference("post")

            val postID = dbRef.push().key!!

            val details = PostVacancyModel(postID, name, desc)

            dbRef.child(postID).setValue(details)
                .addOnCompleteListener{
                    company.text.clear()
                    description.text.clear()
                    Toast.makeText(this, "Post uploaded successfully", Toast.LENGTH_LONG).show()
                }.addOnFailureListener{err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }

        }

    }
}