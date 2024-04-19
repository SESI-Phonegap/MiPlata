package com.sesi.miplata.view.fragment.bmensual.detalle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.sesi.miplata.R
import com.sesi.miplata.databinding.FragmentDetalleMensualBinding
import com.sesi.miplata.view.fragment.bmensual.detalle.viewmodel.MonthlyDetailViewModel
import com.sesi.miplata.view.main.adapter.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MonthlyDetailFragment : Fragment() {

    private lateinit var adapter: ViewPagerAdapter
    private val viewModel: MonthlyDetailViewModel by viewModels()
    private lateinit var binding: FragmentDetalleMensualBinding
    private var month = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetalleMensualBinding.inflate(layoutInflater)
        month = arguments?.getInt("month") ?: 0

        return binding.root
    }

}