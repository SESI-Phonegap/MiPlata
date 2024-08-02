package com.sesi.miplata.util

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.sesi.miplata.R

object PieChartManager {

    fun setupPayChartDayDetail(chart: PieChart, pieData: PieData){
        chart.setUsePercentValues(true)
        chart.description.isEnabled = false
        chart.setExtraOffsets(5f, 10f, 5f, 5f)
        chart.setDragDecelerationFrictionCoef(0.95f)


        //chart.setCenterTextTypeface(Typeface.ITALIC);
        chart.isDrawHoleEnabled = true
        chart.setHoleColor(Color.WHITE)

        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(110)

        chart.holeRadius = 58f
        chart.transparentCircleRadius = 61f

        chart.setDrawCenterText(true)
        chart.setRotationAngle(0F)

        // enable rotation of the chart by touch
        chart.isRotationEnabled = true
        chart.isHighlightPerTapEnabled = true


        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        //chart.setOnChartValueSelectedListener(context)
        chart.animateY(1400, Easing.EaseInOutQuad)


        // chart.spin(2000, 0, 360);
        val l: Legend = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f
        chart.setEntryLabelColor(Color.WHITE)
        chart.setEntryLabelTextSize(10f)

        chart.data = pieData
        chart.highlightValues(null)
        chart.invalidate()
    }

    fun setDataPayChartDayDetail(income:Double, spent:Double, totalIncome:Double, context: Context):PieData{
        var data:PieData = PieData()
        val entries = ArrayList<PieEntry>()
        val ingresoBrutoF: Float = income.toString().toFloat()
        val gastoF: Float = spent.toString().toFloat()
        val ingresoNetoF: Float = totalIncome.toString().toFloat()

        if (ingresoNetoF >= 0){
            //ingreso
            entries.add(PieEntry(ingresoNetoF, "Ingreso Neto", ContextCompat.getDrawable(context, R.drawable.ic_cash_check_white)))
            entries.add(PieEntry(gastoF, "Gastos", ContextCompat.getDrawable(context, R.drawable.ic_cash_remove_white)))
            val dataSet = PieDataSet(entries, "")
            dataSet.setDrawIcons(true)
            dataSet.sliceSpace = 3f
            dataSet.iconsOffset = MPPointF(0f, 40f)
            dataSet.selectionShift = 5f
            dataSet.colors = Utils.getColors()
            //dataSet.setSelectionShift(0f);
            data = PieData(dataSet)
            data.setValueFormatter(PercentFormatter())
            data.setValueTextSize(12f)
            data.setValueTextColor(Color.WHITE)
        }
        return data
    }
}