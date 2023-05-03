package com.example.madproject.adapteres


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madproject.models.PostVacancyModel
import com.example.madproject.R

class CurrentAdapter(private val empList: ArrayList<PostVacancyModel>) :

        RecyclerView.Adapter<CurrentAdapter.ViewHolder>() {

        private lateinit var mListener: OnItemClickListener

        interface OnItemClickListener{
            fun onItemClick(position: Int, btn:String)
        }

        fun setOnItemClickListener(clickListener: OnItemClickListener){
            mListener = clickListener
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_current_list_item, parent, false)
            return ViewHolder(itemView,mListener)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val currentEmp = empList[position]
            holder.tvEmpName.text = currentEmp.companyName
            holder.tvPostDec.text = currentEmp.description
        }

        override fun getItemCount(): Int {
            return empList.size
        }

        class ViewHolder(itemView: View, clickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

            val tvEmpName : TextView = itemView.findViewById(R.id.textView011)
            val tvPostDec : TextView = itemView.findViewById(R.id.textView014)
            private val edtBtn : ImageButton = itemView.findViewById(R.id.imageButton10)
            private val delBtn : ImageButton = itemView.findViewById(R.id.imageButton4)

            init {

                edtBtn.setOnClickListener{
                    clickListener.onItemClick(adapterPosition,"EDT")
                }

                delBtn.setOnClickListener{
                    clickListener.onItemClick(adapterPosition,"DEL")
                }

            }

        }

    }

