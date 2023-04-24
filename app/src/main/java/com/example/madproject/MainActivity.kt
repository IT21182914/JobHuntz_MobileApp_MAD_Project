package com.example.madproject

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.madproject.databinding.ActivityMainBinding
import com.example.madproject.models.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var gsc : GoogleSignInClient
    private lateinit var googleBtn : ImageView
    private lateinit var registerBtn : TextView
    private lateinit var auth : FirebaseAuth
    private lateinit var binding : ActivityMainBinding

    //data holdings

    private lateinit var userEmail :EditText
    private lateinit var password :EditText
    private lateinit var submitBtn :Button

    //database reference

    private lateinit var dbRef : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        //configure google sign in

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("92401905438-ls9vedrdnei1kakq3t58gs4jlhinevk2.apps.googleusercontent.com")
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(this , gso)
        registerBtn = findViewById(R.id.registrationlink)
        googleBtn = findViewById(R.id.google_btn)

        userEmail = findViewById(R.id.email)
        password = findViewById(R.id.password)
        submitBtn = findViewById(R.id.submit_btn)


        submitBtn.setOnClickListener{
            validateData()
        }


        binding.googleBtn.setOnClickListener{
            println("Sign in button clicked")
            signIn()
        }
        registerBtn.setOnClickListener{
            val intent = Intent(applicationContext, UserRegistration::class.java)
            startActivity(intent)
        }


    }

    private fun signIn() {
        val signInIntent = gsc.signInIntent

        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun validateData() {
        dbRef = FirebaseDatabase.getInstance().getReference("data")

        val userEmailadd = userEmail.text.toString()
        val userpass = password.text.toString()

        dbRef.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){

                    for(userSnap in snapshot.children){

                        val userData = userSnap.getValue(UserModel::class.java)

                        if (userData != null) {
                            Log.d("TAG", "email: ${userData.email}\nusername: ${userData.name}")
                        }

                        if (userData != null) {
                            if(userEmailadd == userData.email){
                                userEmail.error = null

                                if (userpass == userData.password){
                                    password.error = null

                                    Toast.makeText(applicationContext,"Successfully logged in", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(applicationContext, Dashboard::class.java)

                                    intent.putExtra(EXTRA_NAME, userData.name)
                                    USER_ID = userData.userId.toString()

                                    Log.d("TAG", "User id is : $USER_ID ")

                                    startActivity(intent)

                                }else{
                                    password.error = "Password is incorrect"
                                }
                            }else{
                                userEmail.error = "This email address is not registered"
                            }
                        }

                        Log.d(TAG, "loadUserData()details $userData")

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        val email = userEmail.text.toString()
        val pass = password.text.toString()

        if (email.isEmpty()) {
            userEmail.error = "Please enter email"
        }
        if (pass.isEmpty()) {
            password.error = "Please enter password"
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)

                    firebaseAuthWithGoogle(account.idToken!!)

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)

            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(applicationContext,"Successfully logged in", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, Dashboard::class.java)
            intent.putExtra(EXTRA_NAME, user.displayName)
            intent.putExtra(EXTRA_NAME, user.displayName)
            intent.putExtra(EXTRA_NAME, user.displayName)
            startActivity(intent)
        }
    }



    companion object{
        const val RC_SIGN_IN = 1001
        var EXTRA_NAME = "EXTRA_NAME"
        var USER_ID = "USER_ID"
    }
}