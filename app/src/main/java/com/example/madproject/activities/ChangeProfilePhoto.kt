package com.example.madproject.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.madproject.R
import com.example.madproject.models.ImageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class ChangeProfilePhoto : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dtabase: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var selectedImage: Uri
    private lateinit var dialog: AlertDialog.Builder
    private lateinit var edtBtn : Button
    private lateinit var image : ImageView
    private lateinit var save : Button
    private lateinit var cancel : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_photo)

        dialog = AlertDialog.Builder(this)
            .setMessage("Updating Profile...")
            .setCancelable(false)

        dtabase = FirebaseDatabase.getInstance().getReference("user")
        storage = FirebaseStorage.getInstance()

        edtBtn = findViewById(R.id.edtProfile)
        save = findViewById(R.id.saveBtn)
        cancel = findViewById(R.id.cancelChangesBtn)

        edtBtn.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }
        save.setOnClickListener{
            if (selectedImage == null){
                Toast.makeText(this, "Please select your Image", Toast.LENGTH_SHORT).show()
            }else{
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
            }
        }
    }

    private fun uploadInfo(imgUrl: String) {
        val key = dtabase.push().key!!
        val user = ImageModel(key, imgUrl)
        dtabase.child(key)
            .setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@ChangeProfilePhoto, Dashboard::class.java))
            }
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