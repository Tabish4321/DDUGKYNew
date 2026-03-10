package com.deendayalproject.adapter



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.model.response.OJTBatchList
import com.deendayalproject.R
import com.deendayalproject.model.response.OJTList
import com.deendayalproject.model.response.OjtListByBatch
import com.deendayalproject.model.response.OjtListByBatch_Res

class OjtListByBatchAdapter(
    private val listener: (OjtListByBatch) -> Unit
) : RecyclerView.Adapter<OjtListByBatchAdapter.BatchViewHolder>() {

    private val items = kotlin.collections.ArrayList<OjtListByBatch>()
    private lateinit var ctx: Context
    private var selectedPosition = -1

    fun setItems(list: List<OjtListByBatch>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class BatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val OJTName: TextView = itemView.findViewById(R.id.OJTName)
        private val No_of_Candidates: TextView = itemView.findViewById(R.id.No_of_Candidates)
        private val Number_of_completed_ojt: TextView = itemView.findViewById(R.id.Number_of_completed_ojt)
        private val tvnumber_of_pending_ojt: TextView = itemView.findViewById(R.id.number_of_pending_ojt)



        fun bind(batch: OjtListByBatch) {


            OJTName.text =   ": "+ batch.employerName
            No_of_Candidates.text = ": "+ batch.numberOfCandidate
            Number_of_completed_ojt.text = ": "+ batch.ojtVerificationCompleted
            tvnumber_of_pending_ojt.text = ": "+ batch.ojtVerificationPending

            itemView.setOnClickListener {
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
            .inflate(R.layout.item_module_ojt_batch_list, parent, false)
        return BatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: BatchViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
