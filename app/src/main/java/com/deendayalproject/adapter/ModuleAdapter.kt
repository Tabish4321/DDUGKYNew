package com.deendayalproject.adapter

import FormAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.R
import com.deendayalproject.model.response.Form
import com.deendayalproject.model.response.Module

class ModuleAdapter(

    private var modules: List<Module>,
    private val onFormClick: (Form) -> Unit

) : RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder>() {

    inner class ModuleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvModuleName: TextView = itemView.findViewById(R.id.tvModuleName)
        val rvForms: RecyclerView = itemView.findViewById(R.id.rvForms)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_module, parent, false)
        return ModuleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ModuleViewHolder, position: Int) {
        val module = modules[position]
        holder.tvModuleName.text = module.moduleName

        // Setup nested RecyclerView for forms
        holder.rvForms.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.rvForms.adapter = FormAdapter(module.forms, onFormClick)

        // Toggle visibility based on isExpanded flag
        holder.rvForms.visibility = if (module.isExpanded) View.VISIBLE else View.GONE

        // Expand/collapse on module name click
        holder.tvModuleName.setOnClickListener {
            module.isExpanded = !module.isExpanded
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = modules.size

    fun updateData(newModules: List<Module>) {
        this.modules = newModules
        notifyDataSetChanged()
    }
}
