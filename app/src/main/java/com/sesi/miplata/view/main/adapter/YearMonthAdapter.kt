package com.sesi.miplata.view.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sesi.miplata.data.dto.SummaryMonthDto
import com.sesi.miplata.databinding.ItemYearMonthSummaryBinding
import com.sesi.miplata.util.DateUtil
import com.sesi.miplata.util.Utils

class YearMonthAdapter(
    private val incomeList: List<SummaryMonthDto>,
    private val spentList: List<SummaryMonthDto>,
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
            binding.tvMonth.text = DateUtil.getMonthByIndex(incomeList[position].month)
            binding.tvIngresoMonto.text = Utils.getCurrencyFormatter(incomeList[position].total)
            binding.tvGastoMonto.text = Utils.getCurrencyFormatter(spentList[position].total)
            binding.root.setOnClickListener {
                action.onClickMont()
            }
        }
    }
}

interface YearMonthAction {
    fun onClickMont()
}