package com.deendayalproject.adapter

import FormAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.databinding.ItemModuleBinding
import com.deendayalproject.model.response.Form
import com.deendayalproject.model.response.Module

class ModuleAdapter(
    private var modules: List<Module>,
    private val onFormClick: (Form) -> Unit
) : RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder>() {

    private var expandedPosition = -1 // Track currently expanded position

    inner class ModuleViewHolder(private val binding: ItemModuleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(module: Module, isExpanded: Boolean) {
            binding.tvModuleName.text = module.moduleName

            // Setup nested RecyclerView for forms
            binding.rvForms.layoutManager = LinearLayoutManager(binding.root.context)
            binding.rvForms.adapter = FormAdapter(module.forms, onFormClick)

            // Update expansion state
            updateExpansionState(isExpanded)

            // Set click listener
            binding.root.setOnClickListener {
                toggleExpansion(adapterPosition)
            }
        }

        private fun updateExpansionState(isExpanded: Boolean) {
            // Update arrow rotation with animation
            binding.ivExpandArrow.animate()
                .rotation(if (isExpanded) 180f else 0f)
                .setDuration(300)
                .start()

            // Update forms visibility
            binding.rvForms.visibility = if (isExpanded) View.VISIBLE else View.GONE

            // Update card elevation
            binding.ivExpandArrow.rotation = if (isExpanded) 12f else 6f

            // Optional: Add scale animation for better visual feedback
            binding.ivExpandArrow.animate()
                .scaleX(if (isExpanded) 1.02f else 1f)
                .scaleY(if (isExpanded) 1.02f else 1f)
                .setDuration(300)
                .start()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleViewHolder {
        val binding = ItemModuleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ModuleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ModuleViewHolder, position: Int) {
        val module = modules[position]
        val isExpanded = position == expandedPosition
        holder.bind(module, isExpanded)
    }

    private fun toggleExpansion(position: Int) {
        val previousExpandedPosition = expandedPosition

        if (expandedPosition == position) {
            // Clicking on already expanded item - collapse it
            expandedPosition = -1
            notifyItemChanged(position)
        } else {
            // Expand new item and collapse previous one
            expandedPosition = position

            // Notify both items for change
            if (previousExpandedPosition != -1) {
                notifyItemChanged(previousExpandedPosition)
            }
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = modules.size

    fun updateData(newModules: List<Module>) {
        // Preserve expansion state if the expanded module still exists
        val previousExpandedModule = if (expandedPosition != -1 && expandedPosition < modules.size) {
            modules[expandedPosition]
        } else null

        modules = newModules

        // Find the same module in new list and set as expanded
        expandedPosition = if (previousExpandedModule != null) {
            newModules.indexOfFirst { it.id == previousExpandedModule.id }
        } else -1

        notifyDataSetChanged()
    }

    // Method to collapse all modules
    fun collapseAll() {
        val previousExpanded = expandedPosition
        expandedPosition = -1
        if (previousExpanded != -1) {
            notifyItemChanged(previousExpanded)
        }
    }

    // Method to get currently expanded position
    fun getExpandedPosition(): Int = expandedPosition
}