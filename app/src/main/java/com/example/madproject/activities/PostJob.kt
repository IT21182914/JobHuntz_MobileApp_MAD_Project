package com.example.madproject.activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.madproject.R

class PostJob : AppCompatActivity() {

    private lateinit var current: Button
    private lateinit var posts: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_jobs)

        current = findViewById(R.id.currentVBtn)
        posts = findViewById(R.id.postVBtn)


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