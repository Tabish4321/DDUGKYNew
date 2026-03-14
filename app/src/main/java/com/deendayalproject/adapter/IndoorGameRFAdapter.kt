package com.deendayalproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.R
import com.deendayalproject.model.IndoorGame
import com.deendayalproject.model.request.TrainingCenter
import com.deendayalproject.model.response.IndoorRFGameResponse
import com.deendayalproject.model.response.IndoorRFGameResponseDetails
import com.deendayalproject.model.response.LivingAreaInformationRes

class IndoorGameRFAdapter(

    private var rooms: List<IndoorRFGameResponseDetails>,
    private var onItemClick:(IndoorRFGameResponseDetails)-> Unit
) : RecyclerView.Adapter<IndoorGameRFAdapter.CenterViewHolder>() {

    inner class CenterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvGameView: TextView = itemView.findViewById(R.id.tvGameView)
        val tvGameName: TextView = itemView.findViewById(R.id.tvGameName)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_indoor_rf_game, parent, false)
        return CenterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CenterViewHolder, position: Int) {
        val room = rooms[position]
     //   holder.tvGameView.text=room.indoorGamePdf
       holder.tvGameName.text=room.indoorGameName


        holder.tvGameView.setOnClickListener {
            onItemClick(room)
        }

    }


    override fun getItemCount(): Int = rooms.size

    fun updateData(newList: List<IndoorRFGameResponseDetails>) {
        rooms = newList
        notifyDataSetChanged()
    }
}

