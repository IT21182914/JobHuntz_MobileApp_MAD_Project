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
import com.google.firebase.database.*
import com.example.madproject.R

    class FetchingActivity : AppCompatActivity() {


    //initialize
    private lateinit var listRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var listingList: ArrayList<ListModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)


        listRecyclerView = findViewById(R.id.rvList)
        listRecyclerView.layoutManager = LinearLayoutManager(this)
        listRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        listingList = arrayListOf<ListModel>()


        //calling getListingData method
        getListingsData()

    }


        //getListingsData method
    private fun getListingsData() {

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
                    val mAdapter = ListAdapter(listingList)
                    listRecyclerView.adapter = mAdapter


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