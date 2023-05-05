package com.example.madproject.activities


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.madproject.R
import com.example.madproject.models.UserModel
import com.google.firebase.database.FirebaseDatabase

class ViewProfile : AppCompatActivity() {

    private lateinit var editBtn : Button
    private lateinit var deleteBtn : Button

    private lateinit var nameFiled : TextView
    private lateinit var emailFiled : TextView
    private lateinit var phoneFiled : TextView
    private lateinit var bioFiled : TextView
    private lateinit var userProfilePhoto : ImageView

    private lateinit var mailTitle : TextView
    private lateinit var phoneTitle : TextView

    private lateinit var name: String
    private lateinit var email : String
    private lateinit var phone : String
    private lateinit var bio : String
    private lateinit var userID : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)

        //getting details from shared preference
        val sp = getSharedPreferences("userSession", Context.MODE_PRIVATE)

        val userNid = sp.getString("userId", "")
        val userName = sp.getString("userName", "")
        val userImage = sp.getString("userImage", "")
        val userPhone = sp.getString("userPhoto", "")
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

        editBtn = findViewById(R.id.editDetails)
        deleteBtn = findViewById(R.id.deleteDetails)
        userProfilePhoto = findViewById(R.id.profile_image2)

        nameFiled = findViewById(R.id.nameFeild)
        emailFiled = findViewById(R.id.emailFeild)
        phoneFiled = findViewById(R.id.phoneFeld)
        bioFiled = findViewById(R.id.bioFeild)

        mailTitle = findViewById(R.id.emailTitle)
        phoneTitle = findViewById(R.id.phoneTitle)

        //setting user image

        Glide.with(this)
            .load(userImage)
            .into(userProfilePhoto)


        if (loggedInUser == "GOOGLE_USER") {

            if (userName != null) {
                name = userName
            }
            Log.d("TAG", "name in Vp : $name")

            if (userBio != null) {
                bio = userBio
            }
            Log.d("TAG", "bio in Vp : $bio")

            nameFiled.text = name
            bioFiled.text = bio

            phoneFiled.visibility = View.GONE
            emailFiled.visibility = View.GONE
            mailTitle.visibility = View.GONE
            phoneTitle.visibility = View.GONE


            editBtn.setOnClickListener {

                val intent = Intent(applicationContext, EditProfileDetails::class.java)
                startActivity(intent)
            }
            deleteBtn.setOnClickListener{

                val intent = Intent(applicationContext, RemoveAccount::class.java)
                startActivity(intent)

            }

        } else if(loggedInUser =="FIREBASE_USER") {

            if (!isLogIn) {

                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)

            } else {
                Log.d("TAG", "USERID SHARED : $userNid")
                Log.d("TAG", "USER NAME SHARED : $userName")
            }

            val dbRef = FirebaseDatabase.getInstance()
            val ref = dbRef.getReference("data")

            val userTempID: String = userNid.toString()

            ref.child(userTempID).get().addOnSuccessListener { dataSnapshot ->

                if (dataSnapshot.exists()) {
                    // Retrieve the value from the DataSnapshot
                    val userData = dataSnapshot.getValue(UserModel::class.java)
                    // Handle the retrieved data
                    if (userData != null) {
                        name = userData.name.toString()
                        email = userData.email.toString()
                        phone = userData.phone.toString()
                        bio = userData.bio.toString()
                        userID  = userData.userId.toString()

                        nameFiled.text = name
                        emailFiled.text = email
                        phoneFiled.text = phone
                        bioFiled.text = bio

                        Log.d("TAG", "Data successfully retrieved")

                        editBtn.setOnClickListener {
                            val intent = Intent(applicationContext, EditProfileDetails::class.java)

                            intent.putExtra("NAME", name)
                            intent.putExtra("BIO", bio)
                            intent.putExtra("PHONE", phone)
                            intent.putExtra("EMAIL", email)

                            startActivity(intent)
                        }
                        deleteBtn.setOnClickListener{

                            val intent = Intent(applicationContext, RemoveAccount::class.java)
                            intent.putExtra("USERID",userID )
                            intent.putExtra("REFID", userNid)
                            startActivity(intent)

                        }
                    }

                } else {
                    // Data does not exist in the specified child node
                    Log.d("TAG", "Data not found")
                }

            }.addOnFailureListener { exception ->
                // Handle any errors that may occur during the retrieval
                Log.e("TAG", "Failed to get data: ${exception.message}")
            }
        }

    }

}