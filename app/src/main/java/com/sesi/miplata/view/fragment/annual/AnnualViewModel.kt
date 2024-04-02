package com.sesi.miplata.view.fragment.annual

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sesi.miplata.data.dto.SummaryMonthDto
import com.sesi.miplata.data.entity.Operaciones
import com.sesi.miplata.data.repository.OperationsV2Repository
import com.sesi.miplata.util.Operations
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import javax.inject.Inject

@HiltViewModel
class AnnualViewModel @Inject constructor(private val repository: OperationsV2Repository) :
    ViewModel() {

    private var _annualOperations = MutableLiveData<List<Operaciones>>()
    var annualOperations: LiveData<List<Operaciones>> = _annualOperations
    private var _summaryMonths = MutableLiveData<List<List<SummaryMonthDto>>>()
    var summaryMonths:LiveData<List<List<SummaryMonthDto>>> = _summaryMonths

    fun getAnnualData(year: Int, context: Context) {
        repository.init(context)
        val initDate = GregorianCalendar(year, Calendar.JANUARY, 1)
        val init = initDate.time
        val endDate = GregorianCalendar(year, Calendar.DECEMBER, 31)
        val end = endDate.time
        _annualOperations.postValue(repository.getOperationsByDate(init.time, end.time))
    }

    fun orderData(operations: List<Operaciones>) {
        getIncomeByMonths(operations)
    }

    private fun getIncomeByMonths(operations: List<Operaciones>) {
        val allIncome =
            operations.filter { operation -> operation.tipoOperacion.equals(Operations.INCOME.type) }
        val allSpent =
            operations.filter { operation -> operation.tipoOperacion.equals(Operations.SPENT.type) }

        if (allIncome.isNotEmpty() || allSpent.isNotEmpty()) {

            val incomeMonths = arrayListOf<List<Operaciones>>()
            val spentMonths = arrayListOf<List<Operaciones>>()

            for (month in Calendar.JANUARY..Calendar.DECEMBER) {
                incomeMonths.add(allIncome.filter { operation ->
                    getByMonth(
                        operation.fecha,
                        month
                    )
                })
                spentMonths.add(allSpent.filter { operation -> getByMonth(operation.fecha, month) })
            }
            val summaryIncomeData = arrayListOf<SummaryMonthDto>()
            incomeMonths.forEachIndexed { index, monthOp ->
                summaryIncomeData.add(getSummaryData(monthOp, index))
            }
            val summarySpentData = arrayListOf<SummaryMonthDto>()
            spentMonths.forEachIndexed { index, monthOp ->
                summarySpentData.add(getSummaryData(monthOp, index))
            }
            val summarySpentIncomeMonths = arrayListOf<List<SummaryMonthDto>>()
            summarySpentIncomeMonths.add(summaryIncomeData)
            summarySpentIncomeMonths.add(summarySpentData)
            this._summaryMonths.postValue(summarySpentIncomeMonths)
        }
    }

    private fun getByMonth(date: Date, month: Int): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.MONTH) == month
    }

    private fun getSummaryData(operations: List<Operaciones>, month: Int): SummaryMonthDto {
        var total = 0.0
        operations.forEach { operation ->
            total += operation.monto
        }
        return SummaryMonthDto(month, total)
    }
}