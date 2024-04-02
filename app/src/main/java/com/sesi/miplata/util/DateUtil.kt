package com.sesi.miplata.util

import java.util.Calendar

object DateUtil {
    fun getYears(): List<String> {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val listOfYears = arrayListOf<String>()
        listOfYears.add(currentYear.toString())
        for (i in 1..5) {
            val pastYear = currentYear - i
            listOfYears.add(pastYear.toString())
        }
        return listOfYears
    }

    fun getMonthByIndex(index: Int): String {
        var monthName: String = "Enero"
        when (index) {
            0 -> {
                monthName = Month.ENERO.textFormat
            }

            1 -> {
                monthName = Month.FEBRERO.textFormat
            }

            2 -> {
                monthName = Month.MARZO.textFormat
            }

            3 -> {
                monthName = Month.ABRIL.textFormat
            }

            4 -> {
                monthName = Month.MAYO.textFormat
            }

            5 -> {
                monthName = Month.JUNIO.textFormat
            }

            6 -> {
                monthName = Month.JULIO.textFormat
            }

            7 -> {
                monthName = Month.AGOSTO.textFormat
            }

            8 -> {
                monthName = Month.SEPTIEMBRE.textFormat
            }

            9 -> {
                monthName = Month.OCTUBRE.textFormat
            }

            10 -> {
                monthName = Month.NOVIEMBRE.textFormat
            }

            11 -> {
                monthName = Month.DICIEMBRE.textFormat
            }
        }
        return monthName
    }
}