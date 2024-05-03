package com.sesi.miplata.data.dto

import java.util.Date

data class SummaryDto(
    var month:Int,
    var total:Double,
    var date: Date?=null
)