package com.deendayalproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.R
import com.deendayalproject.model.response.LivingRoomListItem
import com.deendayalproject.model.response.ToiletItem

class ToiletAdapter(
    private var toiletList: MutableList<ToiletItem>,
    private val onDeleteClick: (ToiletItem) -> Unit
) : RecyclerView.Adapter<ToiletAdapter.ToiletViewHolder>() {

    inner class ToiletViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvToiletId: TextView = itemView.findViewById(R.id.tvToiletId)
        val tvToiletType: TextView = itemView.findViewById(R.id.tvToiletType)
        val ivDelete: ImageView = itemView.findViewById(R.id.ivToiletDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToiletViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_toilet, parent, false)
        return ToiletViewHolder(view)
    }

    override fun getItemCount(): Int = toiletList.size

    override fun onBindViewHolder(holder: ToiletViewHolder, position: Int) {
        val toilet = toiletList[position]
        holder.tvToiletId.text = toilet.rfToiletId.toString()
        holder.tvToiletType.text = toilet.type

        holder.ivDelete.setOnClickListener {
            onDeleteClick(toilet)
        }
    }

    fun updateData(newList: List<ToiletItem>) {
        toiletList.clear()
        toiletList.addAll(newList)
        notifyDataSetChanged()
    }

    fun removeItem(item: ToiletItem) {
        val position = toiletList.indexOf(item)
        if (position != -1) {
            toiletList.removeAt(position)
            notifyItemRemoved(position)
        }
    }



}
