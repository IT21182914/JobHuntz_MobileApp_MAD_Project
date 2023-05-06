package com.example.madproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madproject.adapteres.ListAdapter
import com.example.madproject.models.ListModel
import com.example.madproject.R
import com.google.firebase.database.*

class FetchingActivity : AppCompatActivity() {

    // Declare variables for the views and database reference
    private lateinit var listRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var listingList: ArrayList<ListModel>
    private lateinit var dbRef: DatabaseReference


    // Set up the activity layout and initialize the views
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)


        // Get a reference to the RecyclerView in the layout
        listRecyclerView = findViewById(R.id.rvList)
        listRecyclerView.layoutManager = LinearLayoutManager(this)
        listRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        listingList = arrayListOf<ListModel>()


        // Retrieve the data from the Firebase database
        getListingsData()

    }



    // Define the function to retrieve data from the Firebase database
    private fun getListingsData() {


        // Hide the RecyclerView and show the "loading data" TextView
        listRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Lists")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listingList.clear()
                if (snapshot.exists()){
                    for (listSnap in snapshot.children){

                        val listData = listSnap.getValue(ListModel::class.java)
                        listingList.add(listData!!)
                    }

                    // Create a new adapter with the retrieved data and set it to the RecyclerView
                    val mAdapter = ListAdapter(listingList)
                    listRecyclerView.adapter = mAdapter

                   // Set an item click listener for the RecyclerView items
                    mAdapter.setOnItemClickListener(object : ListAdapter.OnItemClickListener{
                        override fun onItemClick(position: Int) {
                            Log.d("TAG", "listingList size: " + listingList.size)

                            val intent = Intent(this@FetchingActivity, ListingDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("ListId", listingList[position].listId)
                            intent.putExtra("JobName", listingList[position].jobName)
                            intent.putExtra("JobSalary", listingList[position].jobSalary)
                            intent.putExtra("JobDescription", listingList[position].jobDes)
                            intent.putExtra("Benefits", listingList[position].benefitJob)
                            intent.putExtra("CompanyInfo", listingList[position].companyInfo)
                            startActivity(intent)
                        }

                    })

                    listRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}