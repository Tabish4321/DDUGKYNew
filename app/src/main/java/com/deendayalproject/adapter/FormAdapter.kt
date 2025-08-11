import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.R
import com.deendayalproject.model.response.Form

class FormAdapter(
    private val forms: List<Form>,
    private val onFormClick: (Form) -> Unit
) : RecyclerView.Adapter<FormAdapter.FormViewHolder>() {

    inner class FormViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFormName: TextView = itemView.findViewById(R.id.tvFormName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_form, parent, false)
        return FormViewHolder(view)
    }

    override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
        val form = forms[position]
        holder.tvFormName.text = form.formName
        holder.itemView.setOnClickListener {
            onFormClick(form)
        }
    }

    override fun getItemCount(): Int = forms.size
}
