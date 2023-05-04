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
import com.example.madproject.activities.MainActivity.Companion.EXTRA_NAME
import com.example.madproject.activities.MainActivity.Companion.METHOD
import com.example.madproject.activities.MainActivity.Companion.UID
import com.example.madproject.activities.MainActivity.Companion.USER_ID
import com.example.madproject.activities.MainActivity.Companion.USER_PHOTO
import com.example.madproject.databinding.ActivityDashboardBinding
import com.example.madproject.models.GoogleUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


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
    val userPhone = sp.getString("userPhoto", "")
    val userBio = sp.getString("userBio", "")
    val userEmail = sp.getString("userEmail", "")

    val isLogIn = sp.getBoolean("isLoggedIn", false)

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

            val sharedPreferences = getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("is_logged_in", false)
            editor.apply()

            Toast.makeText(applicationContext, "Logged out", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, MainActivity::class.java)

            startActivity(intent)

            finish()
        }

        //if login method is 1001 Google user
        if (intent.getIntExtra(METHOD, 0) == 1001) {

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
                NotificationConfig.notifyObject.notifyHere(this)
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


        } else if (intent.getIntExtra(METHOD, 1) == 1002) {

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
                NotificationConfig.notifyObject.notifyHere(this)
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