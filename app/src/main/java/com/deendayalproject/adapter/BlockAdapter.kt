package com.deendayalproject.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.deendayalproject.R
import com.deendayalproject.model.response.BlockModel

class BlockAdapter(
    context: Context,
    textViewResourceId: Int,
    private var alBlockModel: ArrayList<BlockModel>
) : ArrayAdapter<BlockModel>(context, textViewResourceId, alBlockModel) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val contextData: Context = context

    override fun getCount(): Int {
        return alBlockModel.size
    }

    override fun getItem(position: Int): BlockModel {
        return alBlockModel[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun getItemPosition(blockCode: String): Int {
        var position = 0
        for (i in alBlockModel.indices) {
            if (alBlockModel[i].blockCode.equals(blockCode, ignoreCase = true)) {
                position = i
                break
            }
        }
        return position
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = TextView(contextData)
        label.setTextColor(Color.BLACK)

        label.text = alBlockModel[position].blockName
        return label
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = inflater.inflate(R.layout.spinner_list_item, parent, false)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        tvTitle.setTextColor(Color.BLACK)

        tvTitle.text = alBlockModel[position].blockName
        return view
    }
}
