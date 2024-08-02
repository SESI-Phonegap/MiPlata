package com.sesi.miplata.view.fragment.bmensual.detalle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.sesi.miplata.R
import com.sesi.miplata.data.dto.MonthlyDetailDto
import com.sesi.miplata.data.dto.SummaryDto
import com.sesi.miplata.databinding.FragmentDetalleMensualBinding
import com.sesi.miplata.model.OperacionesModel
import com.sesi.miplata.util.DateUtil
import com.sesi.miplata.util.Month
import com.sesi.miplata.util.MonthlyChartManager
import com.sesi.miplata.util.Operations
import com.sesi.miplata.util.Utils
import com.sesi.miplata.view.fragment.bmensual.detalle.viewmodel.MonthlyDetailViewModel
import com.sesi.miplata.view.main.adapter.SummaryDayAction
import com.sesi.miplata.view.main.adapter.SummaryDayAdapter
import com.sesi.miplata.view.main.adapter.ViewPagerAdapter
import com.sesi.miplata.view.main.adapter.YearMonthAction
import com.sesi.miplata.view.main.adapter.YearMonthAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class MonthlyDetailFragment : Fragment(), SummaryDayAction {

    private val viewModel: MonthlyDetailViewModel by viewModels()
    private lateinit var binding: FragmentDetalleMensualBinding
    private var month = 1
    private var year = "2000"
    private var isRecurrent = false
    private var operations: MonthlyDetailDto? = null
    private var maxDaysOfMonth = 30

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetalleMensualBinding.inflate(layoutInflater)
        month = arguments?.getInt("month") ?: 1
        year = arguments?.getString("year") ?: "2000"
        isRecurrent = arguments?.getBoolean("recurrent") ?: false
        val monthLbl = Utils.getMonth(month.toString())
        binding.tvTitle.text = monthLbl

        val dates = Utils.getDateInitEnd(month.toString(), year)
        viewModel.getOperations(dateInit = dates[0], dateEnd = dates[1], isRecurrent, requireContext())
        observers()
        return binding.root
    }

    private fun observers() {
        viewModel.operation.observe(viewLifecycleOwner) { monthly ->
            val calendar = Calendar.getInstance()
            if (monthly.bills.isNotEmpty()) {
                calendar.time = monthly.bills.first().date
                maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            } else if (monthly.incomes.isNotEmpty()) {
                calendar.time = monthly.incomes.first().date
                maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            }
            viewModel.sumTotalByDay(maxDaysOfMonth, monthly.incomes, monthly.bills)
        }
        viewModel.summaryDays.observe(viewLifecycleOwner){ operations ->
            initChart(incomeList = operations[0], operations[1])
            binding.rvOperaciones.adapter = SummaryDayAdapter(incomeList = operations[0], spentList = operations[1], action = this)
        }
    }

    private fun initChart(incomeList:List<SummaryDto>, billList:List<SummaryDto>){
        binding.chart.clear()
        val income = MonthlyChartManager.generateEntryValues(incomeList)
        val bills = MonthlyChartManager.generateEntryValues(billList)
        val lineData = MonthlyChartManager.configChartData(income, bills, requireContext())
        MonthlyChartManager.setupLineChart(binding.chart,lineData,requireContext())
    }

    override fun onClickDay(date: String) {
        val bundle = Bundle()
        bundle.putString("date", date)
        findNavController().navigate(R.id.action_monthlyDetailFragment_to_dayFragment, bundle)
    }

}