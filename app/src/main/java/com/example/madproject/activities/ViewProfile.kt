package com.example.madproject.activities


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
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

        editBtn = findViewById(R.id.editDetails)
        deleteBtn = findViewById(R.id.deleteDetails)

        nameFiled = findViewById(R.id.nameFeild)
        emailFiled = findViewById(R.id.emailFeild)
        phoneFiled = findViewById(R.id.phoneFeld)
        bioFiled = findViewById(R.id.bioFeild)

        mailTitle = findViewById(R.id.emailTitle)
        phoneTitle = findViewById(R.id.phoneTitle)

        if (intent.getStringExtra("USER") == "GOOGLE_USER") {

            name = intent.getStringExtra("NAME")!!
            Log.d("TAG", "name in Vp : $name")
            bio = intent.getStringExtra("BIO")!!
            Log.d("TAG", "bio in Vp : $bio")

            val sp = getSharedPreferences("userSession", Context.MODE_PRIVATE)

            val userNid = sp.getString("userId", "")
            val userName = sp.getString("userName", "")

            nameFiled.text = userName
            bioFiled.text = bio

            phoneFiled.visibility = View.GONE
            emailFiled.visibility = View.GONE
            mailTitle.visibility = View.GONE
            phoneTitle.visibility = View.GONE


            editBtn.setOnClickListener {

                val intent = Intent(applicationContext, EditProfileDetails::class.java)

                intent.putExtra("NAME", name)
                intent.putExtra("BIO", bio)
                intent.putExtra("ID", intent.getStringExtra("ID"))
                intent.putExtra("USER", "GOOGLE_USER")
                startActivity(intent)
            }
            deleteBtn.setOnClickListener{
                val refid = intent.getStringExtra("REFID")
                Log.d("TAG", "INTENT USERID : $refid")

                val intent = Intent(applicationContext, RemoveAccount::class.java)
                intent.putExtra("REFID", refid)
                intent.putExtra("USER", "GOOGLE_USER")
                startActivity(intent)
            }

        } else {

            var rerfID = ""

            // Check if user is logged in
            val sharedPreferences = getSharedPreferences("userSession", Context.MODE_PRIVATE)
            if (!sharedPreferences.getBoolean("isLoggedIn", false)) {

            } else {
                // User is logged in, retrieve user's session data
                val userId = sharedPreferences.getString("userId", "")
                val userName = sharedPreferences.getString("userName", "")

                if (userId != null) {
                    rerfID = userId
                }

                Log.d("TAG", "USERID SHARED : $userId")
                Log.d("TAG", "USER NAME SHARED : $userName")
                // Do something with user's session data
            }

            val dbRef = FirebaseDatabase.getInstance()
            val ref = dbRef.getReference("data")

            val userTempID: String = MainActivity.USER_ID

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
                            intent.putExtra("USER", "NORMAL_USER")

                            startActivity(intent)
                        }
                        deleteBtn.setOnClickListener{
                            val intent = Intent(applicationContext, RemoveAccount::class.java)
                            intent.putExtra("USERID",userID )
                            intent.putExtra("REFID", rerfID)
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