package com.example.madproject.activities


//importing necessary libraries
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.madproject.R
import com.example.madproject.models.ListModel

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


//creating activity class for jobListForm
class JobListForm : AppCompatActivity() {


    //declare elements
    private lateinit var jobName: EditText
    private lateinit var jobSalary: EditText
    private lateinit var jobDes: EditText
    private lateinit var benefitJob: EditText
    private lateinit var companyInfo: EditText

    private lateinit var btnListSave: Button

    //declare reference to Firebase database
    private lateinit var dbRef: DatabaseReference


    //this method is called when the activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_list_form)

        //assign
        jobName = findViewById(R.id.jobName)
        jobSalary = findViewById(R.id.jobSalary)
        jobDes = findViewById(R.id.jobDes)
        benefitJob = findViewById(R.id.benefitJob)
        companyInfo = findViewById(R.id.companyInfo)
        btnListSave = findViewById(R.id.btnList)


        //get reference to the "Lists" node in Firebase database
        dbRef = FirebaseDatabase.getInstance().getReference("Lists")

        btnListSave.setOnClickListener{
            saveListingData()


        }

    }


    //saveListingData method
    private fun saveListingData(){

        //initialize count for form validation
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

        //if the form is valid, save the data to the Firebase database
        if(count == 0){

            val listId = dbRef.push().key!!

            //create a ListModel object with the input values
            val list = ListModel(listId,jobNameVar,jobSalaryVar,jobDesVar,benefitJobVar,companyInfoVar)

            dbRef.child(listId).setValue(list)
                .addOnCompleteListener{

                    NotificationConfig.notifyObject.notifyHere(this,"New Job Added",
                        "Newly $jobNameVar job available in DARN Job-huntZ application")


                    //show a notification and a success message
                    Toast.makeText(this,"Data inserted successfully",Toast.LENGTH_LONG).show()



                    //clear the input fields
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