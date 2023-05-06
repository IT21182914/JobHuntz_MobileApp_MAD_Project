package com.example.madproject.activities


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.madproject.R


//declaring components
class PostJob : AppCompatActivity() {

    private lateinit var current: Button
    private lateinit var posts: Button
    private lateinit var profileImage : ImageView
    private lateinit var name: TextView
    private lateinit var bio: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_jobs)


        //initialize components
        current = findViewById(R.id.currentVBtn)
        posts = findViewById(R.id.postVBtn)
        profileImage = findViewById(R.id.imageViewProfile)
        name = findViewById(R.id.name)
        bio = findViewById(R.id.bio)

        val sp = getSharedPreferences("userSession", Context.MODE_PRIVATE)

        val userNid = sp.getString("userId", "")
        val userName = sp.getString("userName", "")
        val userImage = sp.getString("userImage", "")
        val userPhone = sp.getString("userPhone", "")
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
        //setting user image
        Glide.with(this)
            .load(userImage)
            .into(profileImage)

        name.text = userName

        bio.text = userBio

        current.setOnClickListener{
            val intent = Intent(this, CurrentVc::class.java)
            startActivity(intent)
        }
        posts.setOnClickListener{
            val intent = Intent(this, PostVC::class.java)
            startActivity(intent)
        }
    }
}