package com.example.madproject.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.madproject.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ChangePassword : AppCompatActivity() {

    private lateinit var edtOldPassword : TextView
    private lateinit var edtNewPassword : TextView
    private lateinit var edtConfirmPassword : TextView
    private lateinit var updateBtn : Button
    private lateinit var cancelbtn : Button

    private lateinit var dbRef : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_passwoed)


        edtOldPassword = findViewById(R.id.oldPassword)
        edtNewPassword = findViewById(R.id.newPassword)
        edtConfirmPassword = findViewById(R.id.confirmPassword)
        updateBtn = findViewById(R.id.updatePasswordBtn)
        cancelbtn = findViewById(R.id.cancelPasswordBtn)



        dbRef = FirebaseDatabase.getInstance().getReference("password")



        if (edtOldPassword.text.toString() == "") {
            //show error
        }

        if(edtOldPassword.text.toString() == "") {
            //show error
        }

        if(edtNewPassword.text.toString() == edtConfirmPassword.text.toString()) {
            //update password
        }
        updateBtn.setOnClickListener {
            //update password
        }
        cancelbtn.setOnClickListener {
            //cancel
        }


    }

}