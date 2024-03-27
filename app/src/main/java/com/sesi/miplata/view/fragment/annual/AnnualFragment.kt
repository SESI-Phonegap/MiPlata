package com.sesi.miplata.view.fragment.annual

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.sesi.miplata.R
import com.sesi.miplata.databinding.FragmentAnnualBinding
import com.sesi.miplata.util.DateUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnnualFragment : Fragment() {

    private lateinit var binding:FragmentAnnualBinding
    private lateinit var adapter:ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ArrayAdapter<String>(requireContext(), R.layout.item_spinner, DateUtil.getYears())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnnualBinding.inflate(layoutInflater)
        binding.spinnerYears.adapter = adapter
        binding.spinnerYears.textAlignment = View.TEXT_ALIGNMENT_CENTER
        return binding.root
    }

}