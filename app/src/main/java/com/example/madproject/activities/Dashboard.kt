package com.example.madproject.activities
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.madproject.R
import com.example.madproject.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth



class Dashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var profileImage : ImageView

//hello
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityDashboardBinding.inflate(layoutInflater)
    setContentView(binding.root)

    //getting details from shared preference
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

    //initialize profile imageView
    profileImage = findViewById(R.id.profile_image)

    //setting user image
    Glide.with(this)
        .load(userImage)
        .into(profileImage)

    if (isLogIn) {

        binding.logoutBtn.setOnClickListener {
            val mAuth = FirebaseAuth.getInstance()
            mAuth.signOut()

            val sharedPreferences = getSharedPreferences("userSession", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putBoolean("is_logged_in", false)
            editor.putString("userID", "")
            editor.putString("userImage", "")
            editor.putString("userName", "")
            editor.putString("userBio", "")
            editor.putString("userEmail", "")
            editor.putString("userPassword", "")
            editor.putBoolean("isAdmin", false)
            editor.putString("loggedInUser", "")

            editor.apply()

            Toast.makeText(applicationContext, "Logged out", Toast.LENGTH_SHORT).show()

            val intent = Intent(applicationContext, MainActivity::class.java)

            startActivity(intent)

            finish()
        }

        //if login method is 1001 Google user
        if (loggedInUser == "GOOGLE_USER") {
            Log.d("TAG", "GOOGLE-USER")

            binding.TextName.text = userName

            //binding profile image
            binding.profileImage.setOnClickListener {
                val intent = Intent(this@Dashboard, ChangeProfilePhoto::class.java)
                intent.putExtra("METHOD", 1001)
                startActivity(intent)
            }
            //binding find job button
            binding.findJob.setOnClickListener {
                val intent = Intent(applicationContext, ViewListingMain::class.java)
                intent.putExtra("METHOD", 1001)
                startActivity(intent)
            }

            //binding post job icon
            binding.postJobs.setOnClickListener {
                val intent = Intent(applicationContext, PostJob::class.java)
                intent.putExtra("METHOD", 1001)
                startActivity(intent)
            }

            //binding categories
            binding.categories.setOnClickListener {
                val intent = Intent(applicationContext, CategoryMain::class.java)
                intent.putExtra("METHOD", 1001)
                startActivity(intent)
            }

            binding.line1.setOnClickListener {

                val intent = Intent(applicationContext, ViewProfile::class.java)
                intent.putExtra("NAME", userName)

                intent.putExtra("BIO", userBio)
                intent.putExtra("ID", googleUserId)
                intent.putExtra("REFID", userRefID)
                intent.putExtra("METHOD", 1001)
                intent.putExtra("USER", "GOOGLE_USER")
                startActivity(intent)
            }
            binding.notifications.setOnClickListener {
                val intent = Intent(applicationContext, Notifications::class.java)
                intent.putExtra("METHOD", 1001)
                startActivity(intent)
            }


        } else if (loggedInUser == "FIREBASE_USER") {
            Log.d("TAG", "FIREBASE-USER")

            binding.TextName.text = userName

            binding.line1.setOnClickListener {
                val intent = Intent(applicationContext, ViewProfile::class.java)
                startActivity(intent)
            }
            //binding profile image
            binding.profileImage.setOnClickListener {
                val intent = Intent(this@Dashboard, ChangeProfilePhoto::class.java)
                intent.putExtra("METHOD", 1002)
                startActivity(intent)
            }
            //binding find job button
            binding.findJob.setOnClickListener {
                val intent = Intent(applicationContext, ViewListingMain::class.java)
                intent.putExtra("METHOD", 1002)
                startActivity(intent)
            }

            //binding post job icon
            binding.postJobs.setOnClickListener {
                val intent = Intent(applicationContext, PostJob::class.java)
                intent.putExtra("METHOD", 1002)
                startActivity(intent)
            }

            //binding categories
            binding.categories.setOnClickListener {
                val intent = Intent(applicationContext, CategoryMain::class.java)
                intent.putExtra("METHOD", 1001)
                startActivity(intent)
            }

            binding.notifications.setOnClickListener {
                val intent = Intent(this@Dashboard, Notifications::class.java)
                intent.putExtra("METHOD", 1002)
                startActivity(intent)
            }
        }

        }else{
            val intent = Intent(this@Dashboard, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}