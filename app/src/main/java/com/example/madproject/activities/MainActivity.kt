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
import com.example.madproject.models.GoogleUser
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

//      initializing auth
        auth = Firebase.auth

        //configure google sign in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("92401905438-ls9vedrdnei1kakq3t58gs4jlhinevk2.apps.googleusercontent.com")
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(this , gso)

        //finding register link
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

    //google sign in function
    private fun signIn() {
        val signInIntent = gsc.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


/* validate function for normal user login with email and password */
    private fun validateData() {

        dbRef = FirebaseDatabase.getInstance().getReference("data")

        var emailCount = 0
        var foundUser : UserModel? = null
        val userEmailAdd = userEmail.text.toString()
        val userPass = password.text.toString()

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

                                //toast message to success full login
                                Toast.makeText(applicationContext,"Successfully logged in", Toast.LENGTH_SHORT).show()

                                //Intent to dashboard
                                val intent = Intent(applicationContext, Dashboard::class.java)

                                //user id
                                USER_ID = foundUser?.userId.toString()

                                //method-normal login
                                intent.putExtra(METHOD, 1002)

                                //send name to dashboard
                                intent.putExtra(EXTRA_NAME, foundUser?.name)

                                //send user ID to dashboard (same id)
                                intent.putExtra(UID, USER_ID)
                                intent.putExtra("USRID", foundUser?.userId)

                                //sending user image link to dashbord
                                intent.putExtra(USER_PHOTO,foundUser?.userImage)

                                //assign same user id
                                userIDs = USER_ID

                                //creating user session
                                val sharedPreferences = getSharedPreferences("userSession", Context.MODE_PRIVATE)

                                // Save user's session data
                                sharedPreferences.edit().apply {
                                    putString("userId", userIDs)
                                    //user details
                                    putString("userID", foundUser?.userId)
                                    putString("userImage", foundUser?.userImage)
                                    putString("userName", foundUser?.name)
                                    putString("userBio", foundUser?.bio)
                                    putString("userEmail", foundUser?.email)
                                    putString("userPassword", foundUser?.password)
                                    putBoolean("isAdmin", foundUser?.isAdmin!!)
                                    putBoolean("isLoggedIn", true)
                                    apply()
                                }
                                //hidingProgressbar
                                hideProgress()

                                // Start Dashboard
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

            //Toast successfully login message
            Toast.makeText(applicationContext,"Successfully logged in", Toast.LENGTH_SHORT).show()

            //create intent to dashboard
            val intent = Intent(applicationContext, Dashboard::class.java)

            //companion object for Google userID
            USID = user.uid

            //ExtraName to send google user name
//            intent.putExtra(EXTRA_NAME, user.displayName)
//
//            //method to identify user
            intent.putExtra(METHOD, RC_SIGN_IN)
//
//            //google user id
//            intent.putExtra(USER_ID, user.uid)
//            //google userPhoto
//            intent.putExtra(USER_PHOTO, user.photoUrl)


            //method for validating google user
            validateGoogleUser(user.uid, user.displayName!!)

            //hidingProgressbar
            hideProgress()
            //start to dashboard
            startActivity(intent)
        }
    }

    private fun validateGoogleUser(googleID: String, googleName: String) {

        //if google user id is not null

        //log to identify user
        Log.d("TAG", "This method is google user ID $googleID")

        //get firebase reference
        val dbRef = FirebaseDatabase.getInstance()
        val ref = dbRef.getReference("data")

        //creating Google user id variable

        //finding user id
        ref.orderByChild("userId").equalTo(googleID)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    //if user exist in firebase db
                    if (dataSnapshot.exists()) {

                        // creating empty variable to assign values
                        var name  = ""
                        var bio  = ""
                        var refId  = ""
                        var userImage = ""

                        // Data exists, retrieve the value
                        for (Dat in dataSnapshot.children) {

                            //fetching data from DB
                            val userData = Dat.getValue(GoogleUser::class.java)

                            //assign values to variables
                            name = userData?.name.toString()
                            bio = userData?.bio.toString()

                            //refId to identify user (firebase ID)
                            refId = userData?.refIId.toString()

                            userImage = userData?.userImage.toString()
                        }

                        //create user session for google user
                        val sharedPreferences = getSharedPreferences("userSession", Context.MODE_PRIVATE)

                        // Save user's session data
                        sharedPreferences.edit().apply {
                            putString("refId", refId)
                            putString("userName", name)
                            putString("userBio", bio)
                            putString("userImage", userImage)
                            putString("googleUserID", googleID)
                            apply()
                        }

                        //if intent needs

                    } else {

                        //new google user
                        // Data does not exist, create a new user
                        //get database reference
                        val dbRefs = FirebaseDatabase.getInstance()
                        val refs = dbRefs.getReference("data")
                        val image = "https://firebasestorage.googleapis.com/v0/b/mad-project-d6d2d.appspot.com/o/Profile%2FnewUser.png?alt=media&token=0051c3d2-aa6b-4886-9b66-7efb12a6cac3"


                        //generate firebase key
                        val refID = refs.push().key!!

                        val user = GoogleUser(
                            googleID,
                            refID,
                            googleName,
                            "Add your bio here.",
                            image
                        )
                        //creating new user under refID
                        refs.child(refID).setValue(user).addOnCompleteListener {

                            //new user created
                            //create user session for google user
                            //get firebase reference
                            val dbRef = FirebaseDatabase.getInstance()
                            val ref = dbRef.getReference("data")

                            //creating Google user id variable

                            //finding user id
                            ref.orderByChild("userId").equalTo(googleID)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        //if user exist in firebase db
                                        if (dataSnapshot.exists()) {
                                            // creating empty variable to assign values
                                            var name  = ""
                                            var bio  = ""
                                            var refId  = ""
                                            var userImage = ""

                                            // Data exists, retrieve the value
                                            for (Dat in dataSnapshot.children) {

                                                //fetching data from DB
                                                val userData = Dat.getValue(GoogleUser::class.java)

                                                //assign values to variables
                                                name = userData?.name.toString()
                                                bio = userData?.bio.toString()

                                                //refId to identify user (firebase ID)
                                                refId = userData?.refIId.toString()

                                                userImage = userData?.userImage.toString()
                                            }

                                            //create user session for google user
                                            val sharedPreferences = getSharedPreferences("userSession", Context.MODE_PRIVATE)

                                            // Save user's session data
                                            sharedPreferences.edit().apply {
                                                putString("refId", refId)
                                                putString("userName", name)
                                                putString("userBio", bio)
                                                putString("userImage", userImage)
                                                putString("googleUserID", googleID)
                                                apply()
                                            }

                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }

                                })

                        }.addOnFailureListener { err ->
                            Toast.makeText(
                                this@MainActivity,
                                "Error: ${err.message} ",
                                Toast.LENGTH_LONG
                            ).show()
                            Log.d("TAG", "USER DOES NOT CREATED")
                        }

                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Handle any errors that may occur during the retrieval
                    Log.e("TAG", "Failed to get data: ${error.message}")
                }
            })
    }


    //function to display progress bar
    private fun showProgress(){
        diaog = Dialog(this@MainActivity)
        diaog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        diaog.setContentView(R.layout.dialog_wait)
        diaog.setCanceledOnTouchOutside(false)
        diaog.show()
    }

    //function to hide progressbar
    private fun hideProgress(){
        diaog.dismiss()
    }

    companion object{
        //method google user
        const val RC_SIGN_IN = 1001

        //name google user
        var EXTRA_NAME = "EXTRA_NAME"

        //id normal user
        var USER_ID = "USER_ID"
        const val UID = "UID"

        //google user photo
        var USER_PHOTO = "USER_PHOTO"

        //method
        var METHOD = "METHOD"

        //companion object for Google userID
        var USID = "UIDS"

    }
}