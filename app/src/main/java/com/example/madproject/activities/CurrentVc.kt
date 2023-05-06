package com.example.madproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madproject.R
import com.example.madproject.activities.DeletePost
import com.example.madproject.activities.PostJob
import com.example.madproject.adapteres.CurrentAdapter
import com.example.madproject.models.PostVacancyModel
import com.google.firebase.database.*

class CurrentVc : AppCompatActivity() {

//declaring components
    private lateinit var currentRecycle : RecyclerView
    private lateinit var tvLoadingData : TextView

    private lateinit var empList: ArrayList<PostVacancyModel>
    private lateinit var dbRef : DatabaseReference
    private lateinit var backBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_vc)

       // initializing variables
        currentRecycle = findViewById(R.id.rvId)
        backBtn = findViewById(R.id.backBtn)

        currentRecycle.layoutManager = LinearLayoutManager(this)

        currentRecycle.setHasFixedSize(true)

        tvLoadingData = findViewById(R.id.tvLoadingData)


        //set on click listener to back button
        backBtn.setOnClickListener{
            val intent = Intent(this, PostJob::class.java)
            startActivity(intent)
            finish()
        }

        empList = arrayListOf()

        getPostListData()
    }

    //creating function to get post list
    private fun getPostListData() {

        currentRecycle.visibility = View.GONE

        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("post")
        Log.d("TAG", "This id $dbRef")
        dbRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                empList.clear()
                if(snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val postData = empSnap.getValue(PostVacancyModel::class.java)
                        empList.add(postData!!)
                    }
                    val mAdapter = CurrentAdapter(empList)
                    currentRecycle.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object :CurrentAdapter.OnItemClickListener{
                        override fun onItemClick(position: Int,btn:String) {
                            if (btn == "EDT"){
                                val intent = Intent(this@CurrentVc, UpdateVacancy::class.java)
                                intent.putExtra("companyName", empList[position].companyName)
                                intent.putExtra("PostID", empList[position].postID)
                                intent.putExtra("description", empList[position].description)
                                startActivity(intent)
                            }else{
                                val intent = Intent(this@CurrentVc, DeletePost::class.java)
                                intent.putExtra("companyName", empList[position].companyName)
                                intent.putExtra("PostID", empList[position].postID)
                                intent.putExtra("description", empList[position].description)
                                startActivity(intent)
                            }

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
