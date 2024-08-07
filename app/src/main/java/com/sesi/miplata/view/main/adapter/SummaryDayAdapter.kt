package com.sesi.miplata.view.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sesi.miplata.R
import com.sesi.miplata.data.dto.SummaryDto
import com.sesi.miplata.databinding.ItemYearMonthSummaryBinding
import com.sesi.miplata.util.Utils
import java.util.Date

class SummaryDayAdapter(
    private val incomeList: List<SummaryDto>,
    private val spentList: List<SummaryDto>,
    private val action: SummaryDayAction

) : RecyclerView.Adapter<SummaryDayAdapter.SummaryDayViewHolder>() {

    inner class SummaryDayViewHolder(val binding: ItemYearMonthSummaryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryDayViewHolder {
        return SummaryDayViewHolder(
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

    override fun onBindViewHolder(holder: SummaryDayViewHolder, position: Int) {
        with(holder){
            binding.tvMonth.text = String.format(itemView.context.getString(R.string.summary_day), incomeList[position].month)
            binding.tvIngresoMonto.text = Utils.getCurrencyFormatter(incomeList[position].total)
            binding.tvGastoMonto.text = Utils.getCurrencyFormatter(spentList[position].total)
            binding.root.setOnClickListener {
                var date:String = ""
                if (incomeList.isNotEmpty() && incomeList.first().date.isNotEmpty()) {
                    date = getDate(incomeList[position].month.toString(), incomeList.first().date)
                }
                if (spentList.isNotEmpty() && spentList.first().date.isNotEmpty()){
                    date = getDate(spentList[position].month.toString(), spentList.first().date)
                }
                if (date.isNotEmpty()) {
                    action.onClickDay(date)
                }
            }
        }
    }

    private fun getDate(day: String, dateMonth: String): String {
        if (dateMonth.isEmpty()) {
            return ""
        }
        val split = dateMonth.split("/")
        return "$day/${split[1]}/${split[2]}"
    }
}
interface SummaryDayAction {
    fun onClickDay(date: String)
}