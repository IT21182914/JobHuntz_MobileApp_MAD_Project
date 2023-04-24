package com.example.madproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class EditProfileDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_details)
        Log.d("TAG", "EditProfile user id ${MainActivity.USER_ID} and ${MainActivity.EXTRA_NAME}")
    }
}