/*
package com.deendayalproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.R
import com.deendayalproject.adapter.LanguageAdapter
import com.deendayalproject.model.request.LanguageItem


class LanguageSelectionFragment : Fragment() {

    private val languages = listOf(
        LanguageItem("English", "अंग्रेज़ी", "en", R.drawable.ic_language_selected),
        LanguageItem("हिन्दी", "हिन्दी", "hi", R.drawable.ic_language_selected),
        LanguageItem("বাংলা", "বাংলা", "bn", R.drawable.ic_language_selected),
        LanguageItem("తెలుగు", "తెలుగు", "te", R.drawable.ic_language_selected),
        LanguageItem("தமிழ்", "தமிழ்", "ta", R.drawable.ic_language_selected),
        LanguageItem("অসমীয়া", "অসমীয়া", "as", R.drawable.ic_language_selected)
    )

    private var selectedPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_language, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewLanguages)

        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = LanguageAdapter(languages, selectedPosition) { position ->
            selectedPosition = position
            recyclerView.adapter?.notifyDataSetChanged() // refresh for selection
            Toast.makeText(
                requireContext(),
                "Selected: ${languages[position].nameEnglish}",
                Toast.LENGTH_SHORT
            ).show()
        }
        recyclerView.adapter = adapter

        return view
    }
}
*/
