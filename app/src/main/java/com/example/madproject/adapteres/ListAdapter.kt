package com.example.madproject.adapteres


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madproject.R
import com.example.madproject.models.ListModel


class ListAdapter(private val listingList: ArrayList<ListModel>):
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }





    fun setOnItemClickListener(clickListener: OnItemClickListener){
        mListener = clickListener
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }



    //OnBineViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentList = listingList[position]
        holder.tvListingName.text = currentList.jobName
    }




    //getItemCount
    override fun getItemCount(): Int {
        return listingList.size
    }





    class ViewHolder(itemView: View, clickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
        val tvListingName : TextView = itemView.findViewById(R.id.tvListingName)
    }



}