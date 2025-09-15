package com.deendayalproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.R
import com.deendayalproject.model.request.TrainingCenter

class TrainingQAdapter(

    private var centers: List<TrainingCenter>,
    private var onItemClick:(TrainingCenter)-> Unit
) : RecyclerView.Adapter<TrainingQAdapter.CenterViewHolder>() {

    inner class CenterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.trainingCenterName)
        val order: TextView = view.findViewById(R.id.senctionOrder)
        val address: TextView = view.findViewById(R.id.trainingCenterAddress)
        val district: TextView =view.findViewById(R.id.districtName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_qteam_layout, parent, false)
        return CenterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CenterViewHolder, position: Int) {
        val center = centers[position]
        // holder.name.text = center.trainingCenterName
        holder.name.text = "Training Center Name: ${center.trainingCenterName}"
        holder.address.text = "Training Center Address: ${center.trainingCenterAddress}"
        holder.order.text = "Sanction Order: ${center.senctionOrder}"
        holder.district.text="District Name: ${center.districtName}"
        holder.itemView.setOnClickListener {
            onItemClick(center)
        }
    }


    override fun getItemCount(): Int = centers.size

    fun updateData(newList: List<TrainingCenter>) {
        centers = newList
        notifyDataSetChanged()
    }
}