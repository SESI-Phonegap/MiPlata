package com.sesi.miplata.util

import java.math.BigDecimal
import java.text.DecimalFormat

object Extensions {

    private val decimalFormat = DecimalFormat("'$'###,###,##0.00")

    fun BigDecimal.toFormat(): String {
        return decimalFormat.format(BigDecimal(this.toPlainString()))
    }

}