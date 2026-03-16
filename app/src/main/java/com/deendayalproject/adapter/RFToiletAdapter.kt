package com.deendayalproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.R
import com.deendayalproject.model.response.LivingAreaInformationRes
import com.deendayalproject.model.response.ToiletRes

class RFToiletAdapter(
    private var toilet: List<ToiletRes>,
    private var onItemClick:(ToiletRes)-> Unit
) : RecyclerView.Adapter<RFToiletAdapter.CenterViewHolder>() {

    inner class CenterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvToiletSerialNo: TextView = itemView.findViewById(R.id.tvToiletSerialNo)
        val tvToiletType: TextView = itemView.findViewById(R.id.tvToiletType)
        val tvLight: TextView = itemView.findViewById(R.id.tvLight)
        val TvFlooring: TextView = itemView.findViewById(R.id.TvFlooring)
        val btnToiletView: TextView = itemView.findViewById(R.id.btnToiletView)

    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rf_toilet_adapter_layout, parent, false)
        return CenterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CenterViewHolder, position: Int) {
        val toilet = toilet[position]
        holder.tvToiletSerialNo.text = (position + 1).toString()
        holder.tvLight.text=toilet.light
        holder.tvToiletType.text=toilet.type
        holder.TvFlooring.text=toilet.flooring


        holder.btnToiletView.setOnClickListener {
            onItemClick(toilet)
        }

    }


    override fun getItemCount(): Int = toilet.size

    fun updateData(newList: List<ToiletRes>) {
        toilet = newList
        notifyDataSetChanged()
    }
}

