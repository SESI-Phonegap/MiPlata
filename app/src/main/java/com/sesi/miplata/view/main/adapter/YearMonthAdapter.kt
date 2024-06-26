package com.sesi.miplata.view.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sesi.miplata.data.dto.SummaryDto
import com.sesi.miplata.databinding.ItemYearMonthSummaryBinding
import com.sesi.miplata.util.DateUtil
import com.sesi.miplata.util.Utils

class YearMonthAdapter(
    private val year:String="",
    private val incomeList: List<SummaryDto>,
    private val spentList: List<SummaryDto>,
    private val action: YearMonthAction
) : RecyclerView.Adapter<YearMonthAdapter.YearMonthViewHolder>() {

    inner class YearMonthViewHolder(val binding: ItemYearMonthSummaryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearMonthViewHolder {
        return YearMonthViewHolder(
            ItemYearMonthSummaryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return incomeList.size
    }

    override fun onBindViewHolder(holder: YearMonthViewHolder, position: Int) {
        with(holder){
            val month = DateUtil.getMonthByIndex(incomeList[position].month)
            binding.tvMonth.text = month
            binding.tvIngresoMonto.text = Utils.getCurrencyFormatter(incomeList[position].total)
            binding.tvGastoMonto.text = Utils.getCurrencyFormatter(spentList[position].total)
            binding.root.setOnClickListener {
                action.onClickMonth(year,incomeList[position].month + 1)
            }
        }
    }
}

interface YearMonthAction {
    fun onClickMonth(year:String, month:Int)
}