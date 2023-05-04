package com.example.madproject.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.madproject.R
import com.example.madproject.models.UserModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserRegistration : AppCompatActivity() {

    private lateinit var name : EditText
    private lateinit var email : EditText
    private lateinit var phone : EditText
    private lateinit var password : EditText
    private lateinit var rePassword : EditText
    private lateinit var regBtn : Button

    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registration)

        name = findViewById(R.id.userRegName)
        email = findViewById(R.id.userRegMail)
        phone = findViewById(R.id.userRegPhone)
        password = findViewById(R.id.regUserPassword)
        rePassword = findViewById(R.id.regUserRePassword)
        regBtn = findViewById(R.id.regBtn)

        dbRef = FirebaseDatabase.getInstance().getReference("data")

        regBtn.setOnClickListener{
            saveUserData()
        }
    }

    private fun saveUserData() {
        //get values
        val userName = name.text.toString()
        val userEmail = email.text.toString()
        val userPhone = phone.text.toString()
        val userPassword = password.text.toString()
        val userRePassword = rePassword.text.toString()

        var count = 0

        if(userName.isEmpty()){
            name.error = "Please enter name"
            count += 1
        }
        if(userEmail.isEmpty()){
            email.error = "Please enter email"
            count += 1
        }
        if(userPhone.isEmpty()){
            phone.error = "Please enter phone number"
            count += 1
        }
        if(userPassword.isEmpty()){
            password.error = "Please enter password"
            count += 1
        }
        if(userRePassword.isEmpty()){
            rePassword.error = "Please enter password"
            count += 1
        }
        if (userPassword != userRePassword){
            rePassword.error = "Re type password correctly"
            count += 1
            print(userPassword)
            print(userRePassword)
        }
       if(count == 0) {

           val userId = dbRef.push().key!!
           val image = "https://firebasestorage.googleapis.com/v0/b/mad-project-d6d2d.appspot.com/o/Profile%2FnewUser.png?alt=media&token=0051c3d2-aa6b-4886-9b66-7efb12a6cac3"

           val user = UserModel(userId, userName, userEmail, userPhone, userPassword,"Enter Your bio here!", image,false)

           dbRef.child(userId).setValue(user)
               .addOnCompleteListener {
                   name.text.clear()
                   email.text.clear()
                   phone.text.clear()
                   password.text.clear()
                   rePassword.text.clear()
                   Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()
               }.addOnFailureListener { err ->
                   Toast.makeText(this, "Error${err.message} ", Toast.LENGTH_LONG).show()
               }
       }else{
           Toast.makeText(this, "Form filled with errors", Toast.LENGTH_SHORT).show()
       }

    }
}