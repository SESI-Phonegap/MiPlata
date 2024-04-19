package com.sesi.miplata.view.fragment.bmensual.detalle.viewmodel

import androidx.lifecycle.ViewModel
import com.sesi.miplata.data.repository.RecurrentIncomeRepository
import com.sesi.miplata.data.repository.RecurrentSpentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MonthlyDetailViewModel @Inject constructor(
    private val incomeRepository: RecurrentIncomeRepository,
    private val spentRepository: RecurrentSpentRepository
): ViewModel() {

}