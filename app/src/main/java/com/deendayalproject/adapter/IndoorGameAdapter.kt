package com.deendayalproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.R
import com.deendayalproject.model.IndoorGame

class IndoorGameAdapter(
    private val games: MutableList<IndoorGame>,
    private val onDeleteClick: (IndoorGame) -> Unit
) : RecyclerView.Adapter<IndoorGameAdapter.IndoorGameViewHolder>() {

    inner class IndoorGameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvGameNumber: TextView = itemView.findViewById(R.id.tvGameNumber)
        val tvGameName: TextView = itemView.findViewById(R.id.tvGameName)
        val ivDelete: ImageView = itemView.findViewById(R.id.ivDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndoorGameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_indoor_game, parent, false)
        return IndoorGameViewHolder(view)
    }

    override fun onBindViewHolder(holder: IndoorGameViewHolder, position: Int) {
        val game = games[position]
        holder.tvGameNumber.text = "Game #${game.gameNumber}"
        holder.tvGameName.text = game.gameName

        holder.ivDelete.setOnClickListener {
            onDeleteClick(game)
        }
    }

    override fun getItemCount(): Int = games.size
}
