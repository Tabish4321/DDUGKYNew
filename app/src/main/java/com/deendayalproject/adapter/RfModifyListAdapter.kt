package com.deendayalproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.R
import com.deendayalproject.model.response.RFModifyListItem




class RfModifyListAdapter(

    private var centers: List<RFModifyListItem>,
    private var onItemClick:(RFModifyListItem)-> Unit
) : RecyclerView.Adapter<RfModifyListAdapter.CenterViewHolder>() {

    inner class CenterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.trainingCenterName)
        val order: TextView = view.findViewById(R.id.senctionOrder)
        val modifyBtn: TextView =view.findViewById(R.id.btnMarkAttendance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_modify_list, parent, false)
        return CenterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CenterViewHolder, position: Int) {
        val center = centers[position]
        holder.name.text = "Training Center Name: ${center.trainingCenterName}"
        holder.order.text = "Sanction Order: ${center.senctionOrder}"
        holder.modifyBtn.setOnClickListener {
            onItemClick(center)
        }
    }


    override fun getItemCount(): Int = centers.size

    fun updateData(newList: List<RFModifyListItem>) {
        centers = newList
        notifyDataSetChanged()
    }
}