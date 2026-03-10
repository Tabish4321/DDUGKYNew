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

class BatchAdapter(
    private val listener: (OJTBatchList) -> Unit
) : RecyclerView.Adapter<BatchAdapter.BatchViewHolder>() {

    private val items = kotlin.collections.ArrayList<OJTBatchList>()
    private lateinit var ctx: Context
    private var selectedPosition = -1

    fun setItems(list: List<OJTBatchList>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class BatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvBatchName: TextView = itemView.findViewById(R.id.tvBatch)
        private val tabMode: TextView = itemView.findViewById(R.id.tabMode)
        private val BatchId: TextView = itemView.findViewById(R.id.batchRegNo)

        private val imgExpand: ImageView = itemView.findViewById(R.id.imgExpand)



        fun bind(batch: OJTBatchList) {
            tvBatchName.text =   ": "+ batch.batchName

            BatchId.text = ": "+ batch.batchRegNo.toString()
            tabMode.text = ": "+ batch.ojt.toString()
            // Arrow icon set based on selected position
//            android:src="@drawable/outline_arrow_forward"
            if (selectedPosition == position) {
                imgExpand.setBackgroundResource(R.drawable.ic_up_arrow)
                val params = imgExpand.layoutParams
                params.width = 40   // pixels me hota hai
                params.height = 40
                imgExpand.layoutParams = params
            } else {
                imgExpand.setBackgroundResource(R.drawable.ic_down_arrow)
                val params = imgExpand.layoutParams
                params.width = 40   // pixels me hota hai
                params.height = 40
                imgExpand.layoutParams = params
            }
            itemView.setOnClickListener {
                // Toggle selection
                selectedPosition =
                    if (selectedPosition == position) -1 else position

                notifyDataSetChanged()
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
            .inflate(R.layout.item_module_ojt_batch, parent, false)
        return BatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: BatchViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
