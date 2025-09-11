package com.deendayalproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.R
import com.deendayalproject.model.request.LanguageItem

class LanguageAdapter(
    private val languages: List<LanguageItem>,
    private val selectedPos: Int,
    private val onLanguageSelected: (Int) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    inner class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val flag: ImageView = itemView.findViewById(R.id.flagIcon)
        val nameText: TextView = itemView.findViewById(R.id.languageName)
        val radioIcon: ImageView = itemView.findViewById(R.id.radioIcon)
        val cardLayout: LinearLayout = itemView.findViewById(R.id.languageCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return LanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val language = languages[position]

        holder.flag.setImageResource(language.flagResId)
        holder.nameText.text = "${language.nameEnglish} / ${language.nameNative}"

        val isSelected = position == selectedPos

        holder.radioIcon.setImageResource(
            if (isSelected) R.drawable.ic_language_selected else R.drawable.ic_language_selected
        )
        holder.cardLayout.setBackgroundResource(
            if (isSelected) R.drawable.language_card_selected else R.drawable.language_card_unselected
        )

        holder.itemView.setOnClickListener {
            onLanguageSelected(position)
        }
    }

    override fun getItemCount(): Int = languages.size
}
