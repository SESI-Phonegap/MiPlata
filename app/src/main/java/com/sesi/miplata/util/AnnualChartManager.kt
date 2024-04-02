package com.sesi.miplata.util

import android.content.Context
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Matrix
import android.os.Build
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.sesi.miplata.R
import com.sesi.miplata.data.dto.SummaryMonthDto

object AnnualChartManager {

    fun setupLineChart(chart: LineChart, data: LineData, context: Context) {
        chart.clear()
        chart.setDrawGridBackground(false);
        chart.viewPortHandler.setMaximumScaleY(5f)
        chart.description = null
        chart.description?.isEnabled = false;
        chart.setDrawBorders(false);

        chart.axisLeft.isEnabled = false;
        chart.axisRight.setDrawAxisLine(false);
        chart.axisRight.setDrawGridLines(false);
        chart.xAxis.setDrawAxisLine(false);
        chart.xAxis.setDrawGridLines(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.isDragEnabled = true;
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);
        chart.resetZoom()

        chart.isHighlightPerTapEnabled = false
        chart.isDragDecelerationEnabled = false // en true hace que al cambiar de datos en la grafica
        // se translade con la velocidad que lleva, y no regreasa a la posicion de inicio o a la
        // preestablecida, es meramente visual
        chart.setVisibleXRangeMaximum(10f)
        chart.setVisibleXRangeMinimum(8f)

        val formatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return getMonths(context)[value.toInt()-1]
            }
        }
        //Set xAxis to Bottom
        val xAxis = chart.xAxis
        xAxis.axisMaximum = data.xMax
        xAxis.axisMinimum = 1f
        xAxis.labelCount = data.xMax.toInt()
        //xAxis.setLabelCount(12, true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawLimitLinesBehindData(false)
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.textColor = chart.context.getColor(R.color.gray_B0B0B2)
        xAxis.valueFormatter = formatter
        xAxis.labelRotationAngle = -65f
        xAxis.textSize = 12f
        xAxis.axisMaximum = data.xMax
        //Set yAxis Hide
        val yAxis = chart.axisLeft
        yAxis.removeAllLimitLines()
        yAxis.axisMaximum =
            data.yMax * 1.1f//para dejar un espacio pequeño arriba y quepan bien los numeros letras
        yAxis.axisMinimum = 0f
        yAxis.setDrawGridLines(false)
        yAxis.setDrawZeroLine(false)
        yAxis.setDrawAxisLine(false)
        yAxis.setDrawLimitLinesBehindData(false)
        yAxis.setDrawLabels(true)
        chart.axisRight.isEnabled = true

        val l = chart.legend
        val arrayLegends = arrayOf<LegendEntry>(
            LegendEntry().apply {
                label = context.getString(R.string.lbl_ingreso)
                formColor = Color.parseColor("#44D62C")
                formSize = 13f
            },
            LegendEntry().apply {
                label = context.getString(R.string.lbl_gastos)
                formColor = Color.parseColor("#5CDFC1")
                formSize = 13f
            }
        )
        l.setCustom(arrayLegends)

        l.isEnabled = true
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.textSize = 12f
        l.yOffset = 10f
        l.textColor = chart.context.getColor(R.color.text_color_green_264444)
        chart.data = data
        //si no se agregan estas funciones de configuracion de la grafica abajo no las respeta :(
        chart.setVisibleXRangeMaximum(10f)
        chart.setVisibleXRangeMinimum(8f)
        chart.setTouchEnabled(true)
        chart.setPinchZoom(true)
        chart.resetViewPortOffsets()
        chart.resetTracking()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            chart.resetPivot()
        }
        chart.resetZoom()
        chart.setDragOffsetX(5f)
        chart.viewPortHandler.refresh(Matrix(), chart, true)
        chart.isHighlightPerTapEnabled = false
        chart.animateX(400)
    }

    fun configChartData(
        incomeValues: List<Entry>,
        spentValues: List<Entry>,
        context: Context
    ): LineData {
        val dataSet = mutableListOf<ILineDataSet>()
        //Ingresos
        val lineData = LineDataSet(incomeValues, context.getString(R.string.lbl_ingreso))
        lineData.lineWidth = 1f
        lineData.circleRadius = 5f
        lineData.setCircleColor(Color.parseColor("#44D62C"))
        lineData.color = Color.parseColor("#264444")
        lineData.circleHoleColor = Color.WHITE
        lineData.circleHoleRadius = 4f
        lineData.setDrawCircleHole(true)
        lineData.valueTextSize = 9f
        lineData.formLineWidth = 1f
        lineData.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
        lineData.formSize = 15f
        lineData.setDrawFilled(true)
        lineData.setDrawIcons(false)
        val gradientDrawable =
            ContextCompat.getDrawable(context, R.drawable.dashboard_gradient_fill)
        lineData.fillDrawable = gradientDrawable
        lineData.setDrawValues(true)
        dataSet.add(lineData)

        //Gastos
        val lineData2 = LineDataSet(spentValues, context.getString(R.string.lbl_gastos))
        lineData2.lineWidth = 1f
        lineData2.circleRadius = 5f
        lineData2.color = Color.parseColor("#264444")
        lineData2.setCircleColor(Color.parseColor("#5CDFC1"))
        lineData2.circleHoleColor = Color.WHITE
        lineData2.circleHoleRadius = 4f
        lineData2.setDrawCircleHole(true)
        lineData2.valueTextSize = 9f
        lineData2.formLineWidth = 1f
        lineData2.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
        lineData2.formSize = 15f
        lineData2.setDrawFilled(true)
        lineData2.setDrawIcons(false)
        val gradientDrawable2 =
            ContextCompat.getDrawable(context, R.drawable.dashboard_gradient_fill_2)
        lineData2.fillDrawable = gradientDrawable2
        lineData2.setDrawValues(true)
        dataSet.add(lineData2)
        return LineData(dataSet)
    }

    fun generateEntryValues(summaryData: List<SummaryMonthDto>): List<Entry> {
        val values = ArrayList<Entry>()
        summaryData.forEach { data ->
            values.add(Entry(data.month.toFloat() + 1, data.total.toFloat()))
        }
        return values
    }

    private fun getMonths(context: Context): List<String> {
        return arrayListOf(
            context.getString(R.string.enero),
            context.getString(R.string.febrero),
            context.getString(R.string.marzo),
            context.getString(R.string.abril),
            context.getString(R.string.mayo),
            context.getString(R.string.junio),
            context.getString(R.string.julio),
            context.getString(R.string.agosto),
            context.getString(R.string.septiembre),
            context.getString(R.string.octubre),
            context.getString(R.string.noviembre),
            context.getString(R.string.diciembre)
        )
    }
}