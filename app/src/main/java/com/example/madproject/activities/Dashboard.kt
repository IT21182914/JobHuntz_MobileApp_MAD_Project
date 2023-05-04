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

//        binding.TextName.text = intent.getStringExtra(EXTRA_NAME)
        profileImage = findViewById(R.id.profile_image)

        Glide.with(this)
            .load(intent.getStringExtra(USER_PHOTO))
            .into(profileImage)

    val sp = getSharedPreferences("userSession", Context.MODE_PRIVATE)

    val userNid = sp.getString("userId", "")
    val userName = sp.getString("userName", "")
    val userImage = sp.getString("userImage", "")
    val userPhone = sp.getString("userPhoto", "")
    val userBio = sp.getString("userBio", "")
    val userEmail = sp.getString("userEmail", "")

    Log.d("TAGD", "Log activity " +
            "\n$userNid and " +
            "\n$userName $userImage" +
            "\n$userPhone" +
            "\n$userBio" +
            "\n$userEmail")



        val uUid = intent.getStringExtra("USRID")
        binding.profileImage.setOnClickListener{
            val intent = Intent(this@Dashboard, ChangeProfilePhoto::class.java)
            intent.putExtra("USRID", uUid)
            startActivity(intent)
        }
        binding.findJob.setOnClickListener{
            val intent = Intent(applicationContext, ViewListingMain::class.java)
            startActivity(intent)
        }
        binding.postJobs.setOnClickListener{
            val intent = Intent(applicationContext, PostJob::class.java)
            startActivity(intent)
        }

        if (intent.getIntExtra(METHOD,0) == 1001){

            if(intent.getStringExtra(USER_ID) != null) {

                Log.d("TAG", "This method is google user ID ${intent.getStringExtra(USER_ID)!!}")

                val dbRef = FirebaseDatabase.getInstance()
                val ref = dbRef.getReference("data")

                val userId = intent.getStringExtra(USER_ID)

                Log.d("TAG", "THIS IS LOG $userId")

                ref.orderByChild("userId").equalTo(userId)
                    .addListenerForSingleValueEvent(object : ValueEventListener {

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            //if user exist in firebase db
                            if (dataSnapshot.exists()) {
                                var name  = ""
                                var bio  = ""
                                var refId  = ""

                                // Data exists, retrieve the value
                                for (Dat in dataSnapshot.children) {
                                    val userData = Dat.getValue(GoogleUser::class.java)
                                    name = userData?.name.toString()
                                    bio = userData?.bio.toString()
                                    refId = userData?.refIId.toString()
                                }

                                val sharedPreferences = getSharedPreferences("userSession", Context.MODE_PRIVATE)

                                // Save user's session data
                                sharedPreferences.edit().apply {
                                    putString("refId", refId)
                                    putString("userName", name)
                                    putString("userBio", bio)
                                    apply()
                                }

                                // Check if user is logged in
                                val sp = getSharedPreferences("userSession", Context.MODE_PRIVATE)

                                    val userNid = sp.getString("userId", "")
                                    val userName = sp.getString("userName", "")
                                    val userImage = sp.getString("userId", "")
                                    val userPhone = sp.getString("userName", "")
                                    val userBio = sp.getString("userId", "")
                                    val userEmail = sp.getString("userName", "")

                                Log.d("TAGD", "Log activity " +
                                        "\n$userNid and " +
                                        "\n$userName $userImage" +
                                        "\n$userPhone" +
                                        "\n$userBio" +
                                        "\n$userEmail")

                                binding.TextName.text = userName

                                binding.line1.setOnClickListener{

                                    val intent = Intent(applicationContext, ViewProfile::class.java)
                                    intent.putExtra("NAME", name)

                                    intent.putExtra("BIO", bio)
                                    intent.putExtra("ID", userId)
                                    intent.putExtra("REFID",refId)

                                    intent.putExtra("USER", "GOOGLE_USER")
                                    startActivity(intent)
                                }



                            } else {
                                binding.TextName.text = intent.getStringExtra(EXTRA_NAME)
                                // Data does not exist, create a new user
                                val dbRefs = FirebaseDatabase.getInstance()
                                val refs = dbRefs.getReference("data")

                                val refID = refs.push().key!!

                                val user = GoogleUser(
                                    userId,
                                    refID,
                                    intent.getStringExtra(EXTRA_NAME),
                                    "Add your bio here."
                                )

                                refs.child(refID).setValue(user).addOnCompleteListener {

                                        binding.line1.setOnClickListener{
                                            val name = intent.getStringExtra(EXTRA_NAME)
                                            val intent = Intent(applicationContext, ViewProfile::class.java)
                                            intent.putExtra("NAME",  name)
                                            intent.putExtra("BIO", "Add your bio here.")
                                            intent.putExtra("ID", userId)
                                            intent.putExtra("REFID", refID)
                                            intent.putExtra("USER", "GOOGLE_USER")
                                            startActivity(intent)
                                        }

                                    }.addOnFailureListener { err ->
                                        Toast.makeText(
                                            this@Dashboard,
                                            "Error: ${err.message} ",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    Log.d("TAG", "USER DOES NOT EXIST")
                                    }

                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            // Handle any errors that may occur during the retrieval
                            Log.e("TAG", "Failed to get data: ${error.message}")
                        }
                    })
            }


        }else if(intent.getIntExtra(METHOD,1) == 1002){
            Log.d("TAG", "\n102 hitted\n")
            Log.d("TAG", "This meth value ${intent.getIntExtra(METHOD, 1)}")
            binding.TextName.text = intent.getStringExtra(EXTRA_NAME)

            if(intent.getStringExtra(UID) != null){
                Log.d("TAG", "This method is firebase user ID ${intent.getStringExtra(UID)!!}")

                binding.line1.setOnClickListener{
                    val intent = Intent(applicationContext, ViewProfile::class.java)
                    startActivity(intent)
                }
            }
        }

        binding.logout.setOnClickListener{
            val mAuth = FirebaseAuth.getInstance()
            mAuth.signOut()

            Toast.makeText(applicationContext,"Logged out", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, MainActivity::class.java)
            finish()
            startActivity(intent)
        }

        binding.notifications.setOnClickListener{
            val intent = Intent(applicationContext, Notifications::class.java)
            startActivity(intent)
        }

    }
}