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
import com.deendayalproject.model.response.VerifiedBatchListSRLM



class BatchSRLMAdapter(
    private val listener: (VerifiedBatchListSRLM) -> Unit
) : RecyclerView.Adapter<BatchSRLMAdapter.BatchViewHolder>() {

    private val items = kotlin.collections.ArrayList<VerifiedBatchListSRLM>()
    private lateinit var ctx: Context
    private var selectedPosition = -1

    fun setItems(list: List<VerifiedBatchListSRLM>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class BatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvBatchName: TextView = itemView.findViewById(R.id.tvBatch)
        private val tabMode: TextView = itemView.findViewById(R.id.tabMode)
        private val BatchId: TextView = itemView.findViewById(R.id.batchRegNo)

        private val imgExpand: ImageView = itemView.findViewById(R.id.imgExpand)



        fun bind(batch: VerifiedBatchListSRLM) {
            tvBatchName.text =   ": "+ batch.courseName

            BatchId.text = ": "+ batch.batchRegNo.toString()
            tabMode.text = ": "+ batch.numberOfOJT.toString()

            itemView.setOnClickListener {

                notifyDataSetChanged()
                listener(batch)


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_module_ojt_batch_srlm, parent, false)
        return BatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: BatchViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
