package com.deendayalproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.R
import com.deendayalproject.model.request.TrainingCenter
import com.deendayalproject.model.response.RfCenter

class RfSrlmAdapter(
    private var centers: List<RfCenter>,
    private var onItemClick:(RfCenter)-> Unit
) : RecyclerView.Adapter<RfSrlmAdapter.CenterViewHolder>() {

    inner class CenterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.trainingCenterName)
        val order: TextView = view.findViewById(R.id.senctionOrder)
        val address: TextView = view.findViewById(R.id.trainingCenterAddress)
        val district: TextView =view.findViewById(R.id.districtName)
        val tvTotalRfCapacity: TextView =view.findViewById(R.id.tvTotalRfCapacity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rf_item_qteam_layout, parent, false)
        return CenterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CenterViewHolder, position: Int) {
        val center = centers[position]

        holder.name.text = "Training Center Name: ${center.trainingCenterName}"
        holder.address.text = "Training Center Address: ${center.trainingCenterAddress}"
        holder.order.text = "Sanction Order: ${center.senctionOrder}"
        holder.district.text="District Name: ${center.districtName}"
        holder.tvTotalRfCapacity.text="Total RF Capacity: ${center.finalRfCapacity}"
        holder.itemView.setOnClickListener {
            onItemClick(center)
        }
    }


    override fun getItemCount(): Int = centers.size

    fun updateData(newList: List<RfCenter>) {
        centers = newList
        notifyDataSetChanged()
    }
}