package com.deendayalproject.fragments

import SharedViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.deendayalproject.databinding.ItCumDomainLabLayoutBinding

class ItCumDomainLabFragment: Fragment() {

    private var _binding: ItCumDomainLabLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ItCumDomainLabLayoutBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        init()


    }
    private fun init(){

        listener()

    }
    private fun listener() {

    }

}