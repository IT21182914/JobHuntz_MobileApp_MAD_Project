package com.example.madproject.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.madproject.R
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madproject.adapteres.NotificationAdapter
import com.example.madproject.models.NotificationModel
import com.example.madproject.models.UserModel
import com.google.firebase.database.*


class Notifications : AppCompatActivity() {

    private lateinit var currentRecycle: RecyclerView
    private lateinit var tvLoadingData: TextView

    private lateinit var empList: ArrayList<NotificationModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        currentRecycle = findViewById(R.id.rvNotification)

        currentRecycle.layoutManager = LinearLayoutManager(this)

        currentRecycle.setHasFixedSize(true)

        backBtn = findViewById(R.id.backBtn)

        tvLoadingData = findViewById(R.id.tvLoadingData)

        backBtn.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            finish()
            startActivity(intent)
        }

        empList = arrayListOf()

        getPostListData()

    }

    private fun removeNotify(notID: String?) {
        Log.d("TAG", "Clicked this $notID")
        val dbRef = FirebaseDatabase.getInstance().getReference("notifications")
        if (notID != null) {
            dbRef.child(notID).removeValue()
                .addOnCompleteListener{
                    Toast.makeText(this, "Notification removed", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{err ->
                    Log.e("error", "Error : ${err.message}")
                }
        }
    }

    private fun getPostListData() {

        currentRecycle.visibility = View.GONE

        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("notifications")
        Log.d("TAG", "This id $dbRef")
        dbRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                empList.clear()
                if (snapshot.exists()) {
                    for (empSnap in snapshot.children) {
                        Log.d("TAG", "his is snap of db$empSnap")
                        val postData = empSnap.getValue(NotificationModel::class.java)
                        Log.d("TAG", "$postData")
                        empList.add(postData!!)
                    }
                    val mAdapter = NotificationAdapter(empList)
                    currentRecycle.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : NotificationAdapter.OnItemClickListener {

                        override fun onItemClick(position: Int) {
                            val note = empList[position].content
                            val notID = empList[position].notId
                            Log.d("TAG", "thsi is note and  its id \n$note\n$notID\n\n")
                            removeNotify(notID)
                        }

                    })
                    currentRecycle.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}
