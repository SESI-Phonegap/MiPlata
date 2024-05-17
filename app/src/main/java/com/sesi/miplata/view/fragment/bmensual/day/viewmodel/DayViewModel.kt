package com.sesi.miplata.view.fragment.bmensual.day.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sesi.miplata.data.dto.MonthlyDetailDto
import com.sesi.miplata.data.repository.CategoryRepository
import com.sesi.miplata.data.repository.OperationsV2Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DayViewModel @Inject constructor(
    private val operationsRepo: OperationsV2Repository,
    private val categoriesRepo: CategoryRepository
) : ViewModel(){

    private var _operations = MutableLiveData<MonthlyDetailDto>()
    var operation: LiveData<MonthlyDetailDto> = _operations

    fun getOperations(dateInit:Long, dateEnd:Long, context: Context) {
        operationsRepo.init(context)
        categoriesRepo.init(context)
        val categories = categoriesRepo.getAll()
        val operationsDay = operationsRepo.getOperationsByDate(dateInit, dateEnd)
        operationsDay
    }

}