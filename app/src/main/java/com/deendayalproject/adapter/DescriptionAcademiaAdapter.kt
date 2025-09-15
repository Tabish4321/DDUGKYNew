package com.deendayalproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.R
import com.deendayalproject.model.RoomModel

class DescriptionAcademiaAdapter(
    private val rooms: List<RoomModel>,
    private val onViewClick: (RoomModel) -> Unit
) : RecyclerView.Adapter<DescriptionAcademiaAdapter.RoomViewHolder>() {

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRoomNo: TextView = itemView.findViewById(R.id.tvRoomNo)
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
        holder.tvRoomNo.text = room.roomNo
        holder.tvLength.text = room.length
        holder.tvWidth.text = room.width
        holder.tvArea.text = room.area
        holder.tvRoomType.text = room.roomType

        holder.btnView.setOnClickListener {
            onViewClick(room)
        }
    }

    override fun getItemCount(): Int = rooms.size
}
