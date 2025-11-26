package com.deendayalproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.databinding.ItemLivingRoomListBinding
import com.deendayalproject.model.response.LivingRoomListItem


class LivingRoomListAdapter(
    private var livingRoomList: MutableList<LivingRoomListItem>,
    private val onDeleteClicked: (LivingRoomListItem) -> Unit
) : RecyclerView.Adapter<LivingRoomListAdapter.LivingRoomViewHolder>() {

    inner class LivingRoomViewHolder(val binding: ItemLivingRoomListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LivingRoomViewHolder {
        val binding =
            ItemLivingRoomListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LivingRoomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LivingRoomViewHolder, position: Int) {
        val item = livingRoomList[position]
        holder.binding.apply {
            tvRoomNo.text = "${item.roomNo}"
            tvLength.text = "${item.length}"
            tvWidth.text = "${item.width}"
            tvMaxCand.text = "${item.totalRfCapacity}"

            ivDelete.setOnClickListener {
                onDeleteClicked(item)
            }
        }
    }

    override fun getItemCount(): Int = livingRoomList.size

    fun updateList(newList: List<LivingRoomListItem>) {
        livingRoomList.clear()
        livingRoomList.addAll(newList)
        notifyDataSetChanged()
    }

    fun removeItem(item: LivingRoomListItem) {
        val index = livingRoomList.indexOfFirst { it.livingRoomId == item.livingRoomId }
        if (index != -1) {
            livingRoomList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

}