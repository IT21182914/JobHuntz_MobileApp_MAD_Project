package com.example.madproject.activities

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.madproject.R
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

    //data holdings for login view
    private lateinit var userEmail :EditText
    private lateinit var password :EditText
    private lateinit var submitBtn :Button

    //database reference
    private lateinit var dbRef : DatabaseReference

    //id
    private lateinit var userIDs :String

    //dialog
    private lateinit var diaog :Dialog

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

        //finding google icon
        googleBtn = findViewById(R.id.google_btn)

        //finding login form components
        userEmail = findViewById(R.id.email)
        password = findViewById(R.id.password)
        submitBtn = findViewById(R.id.submit_btn)

        //add onclick listener to submit button
        submitBtn.setOnClickListener{
            showProgress()
            if(userEmail.text.isEmpty()){
                userEmail.error = "Email must be entered"
                hideProgress()
            }
            if(password.text.isEmpty()){
                password.error = "Password must be entered"
                hideProgress()
            }
            if (!(userEmail.text.isEmpty()  && password.text.isEmpty())){
                //if both email and password is not empty validate those details
                validateData()
            }
        }


        binding.googleBtn.setOnClickListener{
            println("Sign in button clicked")
            showProgress()
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

/* validate function for login */
    private fun validateData() {

        dbRef = FirebaseDatabase.getInstance().getReference("data")

        val userEmailAdd = userEmail.text.toString()
        val userPass = password.text.toString()
        var emailCount = 0
        var foundUser : UserModel? = null

        dbRef.addValueEventListener(object  : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){

                    for(userSnap in snapshot.children){

                        val userData = userSnap.getValue(UserModel::class.java)

                        if (userData != null) {

                            if(userEmailAdd == userData.email.toString()){
                                emailCount += 1
                                foundUser = userData

                            }

                        }
                    }
                    if(emailCount > 0){

                        if(userEmailAdd == foundUser?.email.toString()){

                            userEmail.error = null

                            if (userPass == foundUser?.password.toString()){

                                password.error = null

                                Toast.makeText(applicationContext,"Successfully logged in", Toast.LENGTH_SHORT).show()
                                val intent = Intent(applicationContext, Dashboard::class.java)

                                intent.putExtra(EXTRA_NAME, foundUser?.name)
                                USER_ID = foundUser?.userId.toString()
                                intent.putExtra(UID, USER_ID)
                                intent.putExtra(METHOD, 1002)
                                intent.putExtra(USER_PHOTO,foundUser?.userImage)
                                intent.putExtra("USRID", foundUser?.userId)

                                userIDs = USER_ID

                                val sharedPreferences = getSharedPreferences("userSession", Context.MODE_PRIVATE)

                                // Save user's session data
                                sharedPreferences.edit().apply {
                                    putString("userId", userIDs)
                                    putString("userID", foundUser?.userId)
                                    putString("userImage", foundUser?.userImage)
                                    putString("userName", foundUser?.name)
                                    putString("userBio", foundUser?.bio)
                                    putString("userEmail", foundUser?.email)
                                    putString("userPassword", foundUser?.password)
                                    putBoolean("isLoggedIn", true)
                                    apply()
                                }
                                //hidingProgressbar
                                hideProgress()
                                startActivity(intent)

                            }else{
                                //hidingProgressbar
                                hideProgress()
                                password.error = "Password is incorrect"
                            }
                        }

                    }else{
                        userEmail.error = "This email address is not registere"
                        //hidingProgressbar
                        hideProgress()
                    }

                    Log.d(TAG, "loadUserData()details $foundUser")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
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
                //hidingProgressbar
                hideProgress()
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
                    //hidingProgressbar
                    hideProgress()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(applicationContext,"Successfully logged in", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, Dashboard::class.java)
            USID = user.uid
            intent.putExtra(EXTRA_NAME, user.displayName)
            intent.putExtra(METHOD, RC_SIGN_IN)
            intent.putExtra(USER_ID, user.uid)
            intent.putExtra(USER_PHOTO, user.photoUrl)

            //hidingProgressbar
            hideProgress()
            startActivity(intent)
        }
    }

    private fun showProgress(){
        diaog = Dialog(this@MainActivity)
        diaog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        diaog.setContentView(R.layout.dialog_wait)
        diaog.setCanceledOnTouchOutside(false)
        diaog.show()
    }
    private fun hideProgress(){
        diaog.dismiss()
    }

    companion object{
        const val RC_SIGN_IN = 1001
        var EXTRA_NAME = "EXTRA_NAME"
        var USER_ID = "USER_ID"
        var USER_PHOTO = "USER_PHOTO"
        var METHOD = "METHOD"
        var USID = "UIDS"
        const val UID = "UID"
    }
}