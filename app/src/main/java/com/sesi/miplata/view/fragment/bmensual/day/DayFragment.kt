package com.sesi.miplata.view.fragment.bmensual.day

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.sesi.miplata.R
import com.sesi.miplata.databinding.FragmentDayBinding
import com.sesi.miplata.util.Extensions.toFormat
import com.sesi.miplata.util.Operations
import com.sesi.miplata.util.PieChartManager
import com.sesi.miplata.util.Utils
import com.sesi.miplata.view.fragment.bmensual.day.viewmodel.DayViewModel
import com.sesi.miplata.view.main.adapter.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DayFragment : Fragment() {

    private lateinit var binding: FragmentDayBinding
    private lateinit var adapter: ViewPagerAdapter
    private val operationsArray = arrayOf(Operations.INCOME.type, Operations.SPENT.type)
    private val viewModel: DayViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDayBinding.inflate(layoutInflater)

        val date = arguments?.getString("date")

        val splitDate = date!!.split("/")
        val dates = Utils.getDateInitEnd(
            splitDate[0],
            splitDate[1],
            splitDate[2],
            splitDate[0],
            splitDate[1],
            splitDate[2]
        )
        viewModel.getOperations(dates[0], dates[1], requireContext())
        observers()
        return binding.root
    }

    private fun observers() {
        viewModel.operation.observe(viewLifecycleOwner) { op ->
            adapter = ViewPagerAdapter(
                fragment = parentFragmentManager,
                lifeCycle = lifecycle,
                incomeOperations = op[0],
                billsOperations = op[1],
                operationType = null,
                recurrentIncome = null,
                recurrentSpent = null
            )
            binding.pager.adapter = adapter
            initTabLayout()
            var income = 0.0
            op[0].forEach { incomes ->
                income += incomes.monto
            }
            var totalSpent = 0.0
            op[1].forEach { spent ->
                totalSpent += spent.monto
            }
            val totalIncome = income - totalSpent
            if (totalIncome < 0) {
                binding.tvNegativeBalance.visibility = View.VISIBLE
                binding.tvNegativeBalance.text =
                    String.format(getString(R.string.lbl_negative_balance), totalIncome.toBigDecimal().toFormat())
            } else {
                val pieData = PieChartManager.setDataPayChartDayDetail(
                    income = income,
                    spent = totalSpent,
                    totalIncome = totalIncome,
                    requireContext()
                )
                PieChartManager.setupPayChartDayDetail(binding.chart, pieData)
            }
        }
    }

    private fun initTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = operationsArray[position]
        }.attach()
    }

}