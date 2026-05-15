package com.deendayalproject.adapter

import android.app.DatePickerDialog
import android.content.ClipData
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import com.deendayalproject.R
import com.deendayalproject.model.response.ChildSRLM
import com.deendayalproject.model.response.OJTList
import com.deendayalproject.model.response.OjtBatchRes
import com.deendayalproject.model.response.OjtListChildSRLMRes

import com.deendayalproject.model.response.OjtRes
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class ChildSRLMAdapter(
    private val listener: (ChildSRLM) -> Unit
) : RecyclerView.Adapter<ChildSRLMAdapter.BatchViewHolder>() {

    private val items = kotlin.collections.ArrayList<ChildSRLM>()

    fun setItems(list: List<ChildSRLM>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class BatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val TvcandidateId: TextView = itemView.findViewById(R.id.TvcandidateId)
        val TvcandidatName: TextView = itemView.findViewById(R.id.TvcandidatName)
        val Tvcandidate_status: TextView = itemView.findViewById(R.id.Tvcandidate_status)
        val Tvverification_date: TextView = itemView.findViewById(R.id.Tvverification_date)

        fun bind(batch: ChildSRLM) {

            TvcandidateId.text = batch.candidateId
            TvcandidatName.text = batch.piaName
//            Tvverification_date.text = batch.verificationDate

            val context = itemView.context

//            val verificationValue = batch.verificationStatus
//            val verificationValue = "Complete"


//            when (verificationValue) {
//                "NA" -> {
//
//                    Tvcandidate_status.text ="Add Verification"
//                    Tvcandidate_status.setTextColor(Color.BLUE)
//
//                    // underline add
//                    Tvcandidate_status.paintFlags =
//                        Tvcandidate_status.paintFlags or Paint.UNDERLINE_TEXT_FLAG
//                }
//
//
//                "Completed" -> {
//
//                    Tvcandidate_status.text = "Completed"
////                    Tvcandidate_status.setTextColor(Color.GREEN)
//                    Tvcandidate_status.setTextColor(Color.parseColor("#008000"))
//
//                    // underline add
//                    Tvcandidate_status.paintFlags =
//                        Tvcandidate_status.paintFlags or Paint.UNDERLINE_TEXT_FLAG
//                }
//
//                else -> {
//
//
//                    Tvcandidate_status.setTextColor(Color.BLACK)
//
//                    Tvcandidate_status.paintFlags =
//                        Tvcandidate_status.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
//
//                    Tvcandidate_status.setOnClickListener(null)
//                }
//            }

            // Full item click (optional)
            itemView.setOnClickListener {
                listener(batch)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_child, parent, false)
        return BatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: BatchViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}