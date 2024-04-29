package com.sesi.miplata.view.fragment.bmensual.detalle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sesi.miplata.data.dto.MonthlyDetailDto
import com.sesi.miplata.data.entity.Categorias
import com.sesi.miplata.data.entity.GastosRecurrentesV2
import com.sesi.miplata.data.entity.IngresosRecurrentes
import com.sesi.miplata.data.entity.Operaciones
import com.sesi.miplata.data.repository.CategoryRepository
import com.sesi.miplata.data.repository.OperationsV2Repository
import com.sesi.miplata.data.repository.RecurrentIncomeRepository
import com.sesi.miplata.data.repository.RecurrentSpentRepository
import com.sesi.miplata.model.OperacionesModel
import com.sesi.miplata.util.Operations
import com.sesi.miplata.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MonthlyDetailViewModel @Inject constructor(
    private val incomeRepository: RecurrentIncomeRepository,
    private val spentRepository: RecurrentSpentRepository,
    private val operations: OperationsV2Repository,
    private val categoriesRepo: CategoryRepository
) : ViewModel() {

    private var _operations = MutableLiveData<MonthlyDetailDto>()
    var operation:LiveData<MonthlyDetailDto> = _operations

    fun getOperations(dateInit: Long, dateEnd: Long, withRecurrent: Boolean) {
        var recurrentIncome: List<IngresosRecurrentes?>? = null
        var recurrentSpent: List<GastosRecurrentesV2?>? = null
        val categories = categoriesRepo.getAll()
        if (withRecurrent) {
            recurrentIncome = incomeRepository.getAllMain()
            recurrentSpent = spentRepository.getAllMain()
        }
        val operations = operations.getOperationsByDate(dateInit, dateEnd)
        val spent = Utils.getOperationsByType(operations, Operations.SPENT.type)
        val spentModel = fillModel(spent, categories)
        val orderSpent = spentModel.sortedByDescending { op -> op.fecha }

        val income = Utils.getOperationsByType(operations, Operations.INCOME.type)
        val incomeModel = fillModel(income, categories)
        val orderIncome = incomeModel.sortedByDescending { op -> op.fecha }

        _operations.value = MonthlyDetailDto(
            bills = orderSpent,
            incomes = orderIncome,
            recurrentIncomes = recurrentIncome,
            recurrentBills = recurrentSpent
        )
    }

    private fun fillModel(operations: List<Operaciones>?, categories:List<Categorias>): List<OperacionesModel> {
        val lstOpModel = arrayListOf<OperacionesModel>()
        if (!operations.isNullOrEmpty()) {
            operations.forEach {
                op ->
                categories.forEach {
                    cat ->
                    if (op.idCategoria == cat.id){
                        val operationModel = OperacionesModel().apply {
                            id = op.id
                            name = op.nombre
                            nota = op.nota
                            monto = op.monto
                            idCategoria =op.idCategoria
                            icono = cat.icono
                            catNombre = cat.nombre
                        }
                        lstOpModel.add(operationModel)
                    }
                }
            }
        }
        return lstOpModel
    }

}