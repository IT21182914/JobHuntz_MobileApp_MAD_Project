package com.example.madproject.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.madproject.R
import com.example.madproject.models.ListModel

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class JobListForm : AppCompatActivity() {

    private lateinit var jobName: EditText
    private lateinit var jobSalary: EditText
    private lateinit var jobDes: EditText
    private lateinit var benefitJob: EditText
    private lateinit var companyInfo: EditText

    private lateinit var btnListSave: Button


    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_list_form)


        jobName = findViewById(R.id.jobName)
        jobSalary = findViewById(R.id.jobSalary)
        jobDes = findViewById(R.id.jobDes)
        benefitJob = findViewById(R.id.benefitJob)
        companyInfo = findViewById(R.id.companyInfo)
        btnListSave = findViewById(R.id.btnList)


        dbRef = FirebaseDatabase.getInstance().getReference("Lists")

        btnListSave.setOnClickListener{
            saveListingData()


        }

    }

    private fun saveListingData(){

    var count = 0

        //getting values
        val jobNameVar = jobName.text.toString()
        val jobSalaryVar = jobSalary.text.toString()
        val jobDesVar = jobDes.text.toString()
        val benefitJobVar = benefitJob.text.toString()
        val companyInfoVar = companyInfo.text.toString()





        if(jobNameVar.isEmpty()){
            jobName.error = "Please enter Job name"
            count += 1
        }

        if(jobSalaryVar.isEmpty()){
            jobSalary.error = "Please enter Salary"
            count += 1
        }

        if(jobDesVar.isEmpty()){
            jobDes.error = "Please enter Description"
            count += 1
        }
        if(benefitJobVar.isEmpty()){
            benefitJob.error = "Please enter Benefits"
            count += 1
        }
        if(companyInfoVar.isEmpty()){
            companyInfo.error = "Please enter Company"
            count += 1
        }

        if(count == 0){

            val listId = dbRef.push().key!!


            val list = ListModel(listId,jobNameVar,jobSalaryVar,jobDesVar,benefitJobVar,companyInfoVar)

            dbRef.child(listId).setValue(list)
                .addOnCompleteListener{
                    Toast.makeText(this,"Data inserted successfully",Toast.LENGTH_LONG).show()

                    jobName.text.clear()
                    jobSalary.text.clear()
                    jobDes.text.clear()
                    benefitJob.text.clear()
                    companyInfo.text.clear()


                }.addOnFailureListener{ err ->
                    Toast.makeText(this,"Error ${err.message}",Toast.LENGTH_LONG).show()

                }
        }


    }



}