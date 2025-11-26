package com.deendayalproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.R
import com.deendayalproject.model.response.FieldVerificationItem

class FieldVerificationAdapter(
    private var centers: List<FieldVerificationItem>,
    private var onItemClick: (FieldVerificationItem) -> Unit
) : RecyclerView.Adapter<FieldVerificationAdapter.CenterViewHolder>() {

    inner class CenterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val piaName: TextView = view.findViewById(R.id.piaName)
        val prnNo: TextView = view.findViewById(R.id.prnNo)
        val address: TextView = view.findViewById(R.id.address)
        val districtName: TextView = view.findViewById(R.id.districtName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_field_verification_layout, parent, false)
        return CenterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CenterViewHolder, position: Int) {
        val item = centers[position]
        holder.piaName.text = "PIA Name: ${item.piaName}"
        holder.prnNo.text = "PRN No: ${item.prnNo}"
        holder.address.text = "PIA Address: ${item.address}"
        holder.districtName.text = "District Name: ${item.districtName}"
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount(): Int = centers.size

    fun updateData(newList: List<FieldVerificationItem>) {
        centers = newList
        notifyDataSetChanged()
    }
}
