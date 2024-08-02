package com.sesi.miplata.data.dto

data class CategoryDayDto(
    var categoryId: Long = 0,
    var categoryName: String = "",
    var icon:Int = 0,
    var isSelected:Boolean = false
)