package com.sesi.miplata.view.fragment.annual

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.sesi.miplata.R
import com.sesi.miplata.databinding.FragmentAnnualBinding
import com.sesi.miplata.util.AnnualChartManager
import com.sesi.miplata.util.DateUtil
import com.sesi.miplata.view.main.adapter.YearMonthAction
import com.sesi.miplata.view.main.adapter.YearMonthAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnnualFragment : Fragment(), YearMonthAction {

    private lateinit var binding: FragmentAnnualBinding
    private lateinit var adapter: ArrayAdapter<String>
    private val viewModel: AnnualViewModel by viewModels()
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
        init()
        observers()
        return binding.root
    }

    private fun init() {
        binding.spinnerYears.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.chart.clear()
                viewModel.getAnnualData(
                    (binding.spinnerYears.selectedItem as String).toInt(),
                    requireContext()
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO("Not yet implemented")
            }

        }
    }

    private fun observers() {
        viewModel.annualOperations.observe(viewLifecycleOwner) {
            viewModel.orderData(it)
        }
        viewModel.summaryMonths.observe(viewLifecycleOwner) {
            binding.rvOperaciones.adapter = YearMonthAdapter(it[0], it[1], this)
            val income = AnnualChartManager.generateEntryValues(it[0])
            val spent = AnnualChartManager.generateEntryValues(it[1])
            val lineData = AnnualChartManager.configChartData(income, spent, requireContext())
            AnnualChartManager.setupLineChart(binding.chart, lineData, requireContext())
        }
    }

    override fun onClickMont() {

    }


}