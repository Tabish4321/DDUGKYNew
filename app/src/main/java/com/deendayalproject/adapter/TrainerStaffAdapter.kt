package com.deendayalproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.R
import com.deendayalproject.model.response.Trainer
import com.deendayalproject.model.response.TrainerStaff

class TrainerStaffAdapter(private val list: List<Trainer>) :
    RecyclerView.Adapter<TrainerStaffAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProfileType: TextView = itemView.findViewById(R.id.tvProfileType)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvDesignation: TextView = itemView.findViewById(R.id.tvDesignation)
        val tvEngagementType: TextView = itemView.findViewById(R.id.tvEngagementType)
        val tvDomain: TextView = itemView.findViewById(R.id.tvDomain)
        val tvAssignedCourse: TextView = itemView.findViewById(R.id.tvAssignedCourse)
        val tvTotCert: TextView = itemView.findViewById(R.id.tvTotCert)
        val tvTotCertNo: TextView = itemView.findViewById(R.id.tvTotCertNo)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trainer_staff, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvProfileType.text = "Profile Type: ${item.profileType}"
        holder.tvName.text = "Name: ${item.trainerName}"
        holder.tvDesignation.text = "Designation: ${item.trainerDesignation}"
        holder.tvEngagementType.text = "Engagement Type: ${item.engagementType}"
        holder.tvDomain.text = "Domain/Non-Domain: ${item.domainNondomain}"
        holder.tvAssignedCourse.text = "Assigned Course: ${item.assignedCourse}"
        holder.tvTotCert.text = "TOT Certificate: ${item.totCertificate}"
        holder.tvTotCertNo.text = "TOT Cert. No: ${item.totCertificateNo}"
    }
}
