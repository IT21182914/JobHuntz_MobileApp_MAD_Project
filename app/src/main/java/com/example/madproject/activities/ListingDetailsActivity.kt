package com.example.madproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.madproject.R
import com.example.madproject.models.ListModel
import com.google.firebase.database.FirebaseDatabase

class ListingDetailsActivity : AppCompatActivity() {

    // declare views
    private lateinit var tvListId: TextView
    private lateinit var tvJobName: TextView
    private lateinit var tvJobSalary: TextView
    private lateinit var tvJobDes: TextView
    private lateinit var tvBenefitJob:TextView
    private lateinit var tvCompanyInfo:TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing_details)


        // call methods to initialize and set values to views
        initView()
        setValuesToViews()

        // Update Button setOnClickListener
        btnUpdate.setOnClickListener {

            // log the ListId for check whether it correctly passes
            Log.d("TAG","ID ${intent.getStringExtra("ListId").toString()}")

            // call the method to open update dialog
            openUpdateDialog(
                intent.getStringExtra("ListId").toString(),
                intent.getStringExtra("JobName").toString()
            )
        }


        // Delete Button setOnClickListener
        btnDelete.setOnClickListener {

            // call the method to delete record
            deleteRecord(
                intent.getStringExtra("ListId").toString()
            )
        }


    }


    // Initialize views and assign values to variables
    private fun initView(){

        tvListId = findViewById(R.id.tvListId)
        tvJobName = findViewById(R.id.tvJobName)
        tvJobSalary = findViewById(R.id.tvJobSalary)
        tvJobDes = findViewById(R.id.tvJobDes)
        tvBenefitJob=findViewById(R.id.tvBenefitJob)
        tvCompanyInfo=findViewById(R.id.tvCompanyInfo)


        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDeleteL2)


    }

    // Set values to views
    // set the text of the views to the values passed by the intent
    private fun setValuesToViews(){

        tvListId.text = intent.getStringExtra("ListId")
        tvJobName.text = intent.getStringExtra("JobName")
        tvJobSalary.text = intent.getStringExtra("JobSalary")
        tvJobDes.text = intent.getStringExtra("JobDescription")
        tvBenefitJob.text = intent.getStringExtra("Benefits")
        tvCompanyInfo.text = intent.getStringExtra("CompanyInfo")


    }



    // Open update dialog
    // create a dialog for updating the record
    private fun openUpdateDialog(
        listId: String,
        JobName: String,
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        // create the dialog
        mDialog.setView(mDialogView)


        // get the views from the dialog
        val jobName = mDialogView.findViewById<EditText>(R.id.jobName)
        val jobSalary = mDialogView.findViewById<EditText>(R.id.jobSalary)
        val jobDes = mDialogView.findViewById<EditText>(R.id.jobDes)
        val benefitJob = mDialogView.findViewById<EditText>(R.id.benefitJob)
        val companyInfo = mDialogView.findViewById<EditText>(R.id.companyInfo)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        jobName.setText(intent.getStringExtra("JobName").toString())
        jobSalary.setText(intent.getStringExtra("JobSalary").toString())
        jobDes.setText(intent.getStringExtra("JobDescription").toString())
        benefitJob.setText(intent.getStringExtra("Benefits").toString())
        companyInfo.setText(intent.getStringExtra("CompanyInfo").toString())


        //Set Job Name to the update dialog
        mDialog.setTitle("Updating $JobName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateListingData(
                listId,
                jobName.text.toString(),
                jobSalary.text.toString(),
                jobDes.text.toString(),
                benefitJob.text.toString(),
                companyInfo.text.toString()

            )

            //message for details update
            Toast.makeText(applicationContext, "Listing Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textViews
            tvJobName.text = jobName.text.toString()
            tvJobSalary.text = jobSalary.text.toString()
            tvJobDes.text = jobDes.text.toString()
            tvBenefitJob.text = benefitJob.text.toString()
            tvCompanyInfo.text = companyInfo.text.toString()
            alertDialog.dismiss()
        }
    }



    //Update Job Listing
    private fun updateListingData(
        id: String,
        name: String,
        salary: String,
        description: String,
        benefit: String,
        companyInfo: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Lists").child(id)

        val listInfo = ListModel(id, name, salary, description,benefit,companyInfo)
        dbRef.setValue(listInfo)
    }



    //Delete Job Listing
    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Lists").child(id)
        Log.d("TAG","firebaseRef $dbRef")
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Listing data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }



}