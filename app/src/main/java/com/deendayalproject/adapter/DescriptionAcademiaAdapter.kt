package com.deendayalproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.R
import com.deendayalproject.model.response.RoomItem

class DescriptionAcademiaAdapter(
    private val rooms: MutableList<RoomItem>,
    private val onViewClick: (RoomItem) -> Unit
) : RecyclerView.Adapter<DescriptionAcademiaAdapter.RoomViewHolder>() {

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMaxCandidate: TextView = itemView.findViewById(R.id.tvMaxCandidate)
        val tvLength: TextView = itemView.findViewById(R.id.tvLength)
        val tvWidth: TextView = itemView.findViewById(R.id.tvWidth)
        val tvArea: TextView = itemView.findViewById(R.id.tvArea)
        val tvRoomType: TextView = itemView.findViewById(R.id.tvRoomType)
        val btnView: TextView = itemView.findViewById(R.id.btnView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.description_academia_layout, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.tvMaxCandidate.text = room.maxPermissibleCandidate
        holder.tvLength.text = room.roomLength
        holder.tvWidth.text = room.roomWidth
        holder.tvArea.text = room.roomArea
        holder.tvRoomType.text = room.roomType

        holder.btnView.setOnClickListener {
            onViewClick(room)
        }
    }

    override fun getItemCount(): Int = rooms.size
}
