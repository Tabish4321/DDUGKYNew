package com.deendayalproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.R
import com.deendayalproject.model.request.TrainingCenter
import com.deendayalproject.model.response.LivingAreaInformationRes

class LivingAreaInformationAdapter(
    private var rooms: List<LivingAreaInformationRes>,
    private var onItemClick:(LivingAreaInformationRes)-> Unit
) : RecyclerView.Adapter<LivingAreaInformationAdapter.CenterViewHolder>() {

    inner class CenterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvRoomNumber: TextView = itemView.findViewById(R.id.tvRoomNumber)
        val tvRoomArea: TextView = itemView.findViewById(R.id.tvRoomArea)
        val maxCandidate: TextView = itemView.findViewById(R.id.maxCandidate)
        val btnView: TextView = itemView.findViewById(R.id.btnView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.living_aera_infomation_adapter_layout, parent, false)
        return CenterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CenterViewHolder, position: Int) {
        val room = rooms[position]
        holder.tvRoomNumber.text=room.roomNo.toString()
        holder.tvRoomArea.text=room.area
        holder.maxCandidate.text=room.totalRfCapacity


        holder.btnView.setOnClickListener {
            onItemClick(room)
        }

    }


    override fun getItemCount(): Int = rooms.size

    fun updateData(newList: List<LivingAreaInformationRes>) {
        rooms = newList
        notifyDataSetChanged()
    }
}
