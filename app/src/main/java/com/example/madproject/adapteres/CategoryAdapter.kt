package com.example.madproject.adapteres

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madproject.R
import com.example.madproject.models.CategoryModel

class CategoryAdapter(private val empList: ArrayList<CategoryModel>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int, mode: String)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_category_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentEmp = empList[position]
        holder.tvEmpName.text = currentEmp.categoryName
    }

    override fun getItemCount(): Int {
        return empList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val tvEmpName : TextView = itemView.findViewById(R.id.tvEmpName)
        private val edtBtn : ImageView = itemView.findViewById(R.id.editIcon)
        private val delBtn : ImageView = itemView.findViewById(R.id.deleteIcon)

        init {
            edtBtn.setOnClickListener{
                clickListener.onItemClick(adapterPosition, "EDT")
            }
            delBtn.setOnClickListener{
                clickListener.onItemClick(adapterPosition, "DEL")
            }

        }

    }

}
