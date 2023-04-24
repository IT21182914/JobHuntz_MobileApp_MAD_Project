package com.example.madproject

//import com.google.android.gms.auth.api.signin.GoogleSignInClient
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.madproject.MainActivity.Companion.EXTRA_NAME
import com.example.madproject.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth


class Dashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.TextName.text = intent.getStringExtra(EXTRA_NAME)

        binding.logout.setOnClickListener{

            val mAuth = FirebaseAuth.getInstance()
            mAuth.signOut()

            Toast.makeText(applicationContext,"Logged out", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()


        }

        binding.line1.setOnClickListener{
            val intent = Intent(applicationContext, ViewProfile::class.java)
            startActivity(intent)
        }

        binding.notifications.setOnClickListener{
            val intent = Intent(applicationContext, Notifications::class.java)
            startActivity(intent)
        }

    }
}