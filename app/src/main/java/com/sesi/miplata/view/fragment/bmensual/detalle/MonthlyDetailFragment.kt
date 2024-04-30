package com.sesi.miplata.view.fragment.bmensual.detalle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.sesi.miplata.data.dto.MonthlyDetailDto
import com.sesi.miplata.databinding.FragmentDetalleMensualBinding
import com.sesi.miplata.util.Operations
import com.sesi.miplata.util.Utils
import com.sesi.miplata.view.fragment.bmensual.detalle.viewmodel.MonthlyDetailViewModel
import com.sesi.miplata.view.main.adapter.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MonthlyDetailFragment : Fragment() {

    private lateinit var adapter: ViewPagerAdapter
    private val viewModel: MonthlyDetailViewModel by viewModels()
    private lateinit var binding: FragmentDetalleMensualBinding
    private var month = 1
    private var year = "2000"
    private var isRecurrent = false
    private var operations: MonthlyDetailDto? = null
    private val operationsArray = arrayOf(Operations.INCOME.type, Operations.SPENT.type)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetalleMensualBinding.inflate(layoutInflater)
        month = arguments?.getInt("month") ?: 1
        year = arguments?.getString("year") ?: "2000"
        isRecurrent = arguments?.getBoolean("recurrent") ?: false

        val dates = Utils.getDateInitEnd(month.toString(), year)
        viewModel.getOperations(dateInit = dates[0], dateEnd = dates[1], isRecurrent, requireContext())
        observers()
        return binding.root
    }

    private fun observers() {
        viewModel.operation.observe(viewLifecycleOwner) { monthly ->
            operations = monthly
            adapter = ViewPagerAdapter(
                parentFragmentManager,
                lifecycle,
                monthly.bills,
                monthly.incomes,
                monthly.recurrentIncomes,
                monthly.recurrentBills,
                Operations.INCOME
            )
            binding.pager.adapter = adapter
            initTabLayout()
        }
    }

    private fun initTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.pager) {
            tab, position ->
            tab.text = operationsArray[position]
        }.attach()
    }

}