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
}