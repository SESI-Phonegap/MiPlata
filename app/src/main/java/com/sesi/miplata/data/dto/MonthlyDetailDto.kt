package com.sesi.miplata.data.dto

import com.sesi.miplata.data.entity.GastosRecurrentesV2
import com.sesi.miplata.data.entity.IngresosRecurrentes
import com.sesi.miplata.model.OperacionesModel

data class MonthlyDetailDto(
    var bills:List<OperacionesModel>,
    var incomes:List<OperacionesModel>,
    var recurrentIncomes: List<IngresosRecurrentes?>?,
    var recurrentBills: List<GastosRecurrentesV2?>?
)