package com.deendayalproject.adapter



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.R
import com.deendayalproject.model.response.ListByBatchSRLM
import com.deendayalproject.model.response.OjtListByBatch
import com.deendayalproject.util.AppUtil

class OjtListByBatchSRLMAdapter(
    private val listener: (ListByBatchSRLM) -> Unit
) : RecyclerView.Adapter<OjtListByBatchSRLMAdapter.BatchViewHolder>() {

    private val items = kotlin.collections.ArrayList<ListByBatchSRLM>()
    private lateinit var ctx: Context
    private var selectedPosition = -1

    fun setItems(list: List<ListByBatchSRLM>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class BatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val OJTName: TextView = itemView.findViewById(R.id.OJTName)
        private val verificationStatus: TextView = itemView.findViewById(R.id.number_of_pending_ojt)
        private val CandidateName: TextView = itemView.findViewById(R.id.No_of_Candidates)
        private val verificationDate: TextView = itemView.findViewById(R.id.Number_of_completed_ojt)
        private val ojtPlanId: TextView = itemView.findViewById(R.id.ojtPlanId)
        private val CandidateId: TextView = itemView.findViewById(R.id.OJTName)



        fun bind(batch: ListByBatchSRLM) {



            verificationStatus.text =   ": "+ batch.verificationStatus
            ojtPlanId.text =   ": "+ batch.ojtPlanId
            verificationDate.text =   ": "+ batch.verificationDate
            CandidateName.text =   ": "+ batch.candidateName
            CandidateId.text =   ": "+ batch.candidateId

            itemView.setOnClickListener {
                AppUtil.saveOJTCandidateIDPreference(itemView.context, CandidateId.toString())
                listener(batch)

//                Toast.makeText(
//                    itemView.context,
//                    "Form submitted successfully",
//                    Toast.LENGTH_SHORT
//                ).show()

            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_module_ojt_batch_srlm_list, parent, false)
        return BatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: BatchViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
