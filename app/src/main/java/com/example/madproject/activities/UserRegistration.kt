package com.example.madproject.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.madproject.R
import com.example.madproject.models.UserModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserRegistration : AppCompatActivity() {

    //declare variables
    private lateinit var name : EditText
    private lateinit var email : EditText
    private lateinit var phone : EditText
    private lateinit var password : EditText
    private lateinit var rePassword : EditText
    private lateinit var regBtn : Button
    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registration)

        //initialize variables
        name = findViewById(R.id.userRegName)
        email = findViewById(R.id.userRegMail)
        phone = findViewById(R.id.userRegPhone)
        password = findViewById(R.id.oldPassword)
        rePassword = findViewById(R.id.confirmPassword)
        regBtn = findViewById(R.id.regBtn)

        //get database reference
        dbRef = FirebaseDatabase.getInstance().getReference("data")

        //set on click listener to register button
        regBtn.setOnClickListener{
            //call save user data function
            saveUserData()
        }
    }

    //function to save user data
    private fun saveUserData() {
        //get values
        val userName = name.text.toString()
        val userEmail = email.text.toString()
        val userPhone = phone.text.toString()
        val userPassword = password.text.toString()
        val userRePassword = rePassword.text.toString()

        //create variable to count errors
        var isDetailsValid = false

        //check if fields are empty
        if(userName.isEmpty()){
            name.error = "Please enter name"
            isDetailsValid = true
        }

        if(userEmail.isEmpty()){
            email.error = "Please enter email"
            isDetailsValid = true
        }else{
            //check if email is valid
            if(!(emailValidation(userEmail))){
                email.error = "Invalid email format"
                isDetailsValid = true
            }
        }
        if(userPhone.isEmpty()){
            phone.error = "Please enter phone number"
            isDetailsValid = true
        }else{
            //check if phone number is valid
            if(!(phoneValidation(phone))){
                phone.error = "Invalid phone number format"
                isDetailsValid = true
            }
        }

        if(userPassword.isEmpty()){
            password.error = "Please enter password"
            isDetailsValid = true
        }else{
            //check if password is valid
            if (!(passwordValidation(password))){
                password.error = "Password must be at least 8 characters long and contain at least one digit, one lowercase letter, one uppercase letter, and one special character (@#$%^&+=)"
                isDetailsValid = true
            }
        }

        if(userRePassword.isEmpty()){
            rePassword.error = "Please enter password"
            isDetailsValid = true
        }else{
            //check if password is valid
            if (!(passwordValidation(rePassword))){
                password.error = "Password must be at least 8 characters long and contain at least one digit, one lowercase letter, one uppercase letter, and one special character (@#$%^&+=)"
                isDetailsValid = true
            }
        }
        //check if password and retype password are same
        if (userPassword != userRePassword){
            rePassword.error = "Re type password correctly"
            isDetailsValid = true
            print(userPassword)
            print(userRePassword)
        }
        //check if password is valid
        if (!(passwordValidation(password))){
            isDetailsValid = false
        }

        //if no errors
       if(!(isDetailsValid)) {

           //create new user object
           //create new user id
           val userId = dbRef.push().key!!

           //set default image
           val image = "https://firebasestorage.googleapis.com/v0/b/mad-project-d6d2d.appspot.com/o/Profile%2FnewUser.png?alt=media&token=0051c3d2-aa6b-4886-9b66-7efb12a6cac3"

           //create new user object
           val user = UserModel(userId, userName, userEmail, userPhone, userPassword,"Enter Your bio here!", image,false)

           //save user data to database
           dbRef.child(userId).setValue(user)
               .addOnCompleteListener {
                   name.text.clear()
                   email.text.clear()
                   phone.text.clear()
                   password.text.clear()
                   rePassword.text.clear()

                   //call notification function to send notification to user
                   NotificationConfig.notifyObject.notifyHere(this,"New account created", "Congratulations!! $userName You have successfully created new account in DARN Job-huntZ application")

                   Toast.makeText(this, "New account successfully created", Toast.LENGTH_LONG).show()
               }.addOnFailureListener { err ->
                   Toast.makeText(this, "Error${err.message} ", Toast.LENGTH_LONG).show()
               }
       }else{
           Toast.makeText(this, "Form filled with errors", Toast.LENGTH_SHORT).show()
       }

    }
    //email validation function
    private fun emailValidation(email : String) : Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    //password validation function
    private fun passwordValidation(password: EditText): Boolean {
        //password validation
        val passwordPattern = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")

        val passwordVal = password.text.toString()

        return passwordVal.matches(passwordPattern)
    }
    //phone number validation function
    private fun phoneValidation(phone: EditText): Boolean {
        //phone number validation
        val phonePattern = Regex("^[0-9]{10}$")

        val phoneVal = phone.text.toString()

        return phoneVal.matches(phonePattern)
    }

}