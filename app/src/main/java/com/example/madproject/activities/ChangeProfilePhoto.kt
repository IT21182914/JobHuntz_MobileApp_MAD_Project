package com.example.madproject.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.madproject.R
import com.example.madproject.models.UserModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class ChangeProfilePhoto : AppCompatActivity() {

    private lateinit var dtabase: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var selectedImage: Uri
    private lateinit var dialog: AlertDialog.Builder
    private lateinit var edtBtn : Button
    private lateinit var image : ImageView
    private lateinit var save : Button
    private lateinit var cancel : Button
    private lateinit var currentImage : ImageView

    private lateinit var diaog :Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_photo)



        dtabase = FirebaseDatabase.getInstance().getReference("data")
        storage = FirebaseStorage.getInstance()

        currentImage = findViewById(R.id.profile_image2)
        edtBtn = findViewById(R.id.edtProfile)
        save = findViewById(R.id.saveBtn)
        cancel = findViewById(R.id.cancelChangesBtn)

        edtBtn.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        val sp = getSharedPreferences("userSession", Context.MODE_PRIVATE)

        val userNid = sp.getString("userId", "")
        val userName = sp.getString("userName", "")
        val userImage = sp.getString("userImage", "")
        val userPhone = sp.getString("userPhoto", "")
        val userBio = sp.getString("userBio", "")
        val userEmail = sp.getString("userEmail", "")

        Log.d("TAGD", "Log activity in Update image " +
                "\n$userNid and " +
                "\n$userName " +
                "\n$userImage" +
                "\n$userPhone" +
                "\n$userBio" +
                "\n$userEmail")

        Glide.with(this)
            .load(userImage)
            .into(currentImage)

        save.setOnClickListener{
            if (selectedImage == null){
                Toast.makeText(this, "Please select your Image", Toast.LENGTH_SHORT).show()
            }else{
                showProgress()
                saveDat()
            }
        }
        cancel.setOnClickListener{
            val intent = Intent(this@ChangeProfilePhoto, Dashboard::class.java)
            startActivity(intent)
        }


    }

    private fun saveDat() {
        val ref = storage.reference.child("Profile").child(Date().time.toString())
        ref.putFile(selectedImage).addOnCompleteListener{
            if(it.isSuccessful){
                 ref.downloadUrl.addOnSuccessListener { task ->
                     uploadInfo(task.toString())
                 }
            }else{
                hideProgress()
            }
        }
    }

    private fun uploadInfo(imgUrl: String) {
        val user = UserModel( imgUrl)
        val idUsr = intent.getStringExtra("USRID").toString()
        dtabase.child(idUsr)
            .child("userImage")
            .setValue(imgUrl)
            .addOnSuccessListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show()
                hideProgress()
                val intent = Intent(this@ChangeProfilePhoto, Dashboard::class.java)
                intent.putExtra(MainActivity.METHOD, 1002)
                intent.putExtra(MainActivity.UID, MainActivity.USER_ID)
                startActivity(intent)

            }.addOnFailureListener{
                hideProgress()
            }
    }
    private fun showProgress(){
        diaog = Dialog(this@ChangeProfilePhoto)
        diaog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        diaog.setContentView(R.layout.dialog_wait)
        diaog.setCanceledOnTouchOutside(false)
        diaog.show()
    }
    private fun hideProgress(){
        diaog.dismiss()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data != null){
            if (data.data != null){
                selectedImage = data.data!!

                image = findViewById(R.id.profile_image2)
                image.setImageURI(selectedImage)

            }
        }
    }
}