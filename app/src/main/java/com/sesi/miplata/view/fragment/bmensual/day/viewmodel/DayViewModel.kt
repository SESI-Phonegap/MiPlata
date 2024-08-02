package com.sesi.miplata.view.fragment.bmensual.day.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sesi.miplata.data.dto.MonthlyDetailDto
import com.sesi.miplata.data.entity.Categorias
import com.sesi.miplata.data.entity.Operaciones
import com.sesi.miplata.data.repository.CategoryRepository
import com.sesi.miplata.data.repository.OperationsV2Repository
import com.sesi.miplata.model.OperacionesModel
import com.sesi.miplata.util.Operations
import com.sesi.miplata.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DayViewModel @Inject constructor(
    private val operationsRepo: OperationsV2Repository,
    private val categoriesRepo: CategoryRepository
) : ViewModel(){

    private var _operations = MutableLiveData<List<List<OperacionesModel>>>()
    var operation: LiveData<List<List<OperacionesModel>>> = _operations

    fun getOperations(dateInit:Long, dateEnd:Long, context: Context) {
        operationsRepo.init(context)
        categoriesRepo.init(context)
        val categories = categoriesRepo.getAll()
        val operationsDay = operationsRepo.getOperationsByDate(dateInit, dateEnd)
        val spent = Utils.getOperationsByType(operationsDay, Operations.SPENT.type)
        val spentModel = fillModel(spent, categories)
        val income = Utils.getOperationsByType(operationsDay, Operations.INCOME.type)
        val incomeModel = fillModel(income, categories)
        val opModel = arrayListOf<List<OperacionesModel>>()
        opModel.add(incomeModel)
        opModel.add(spentModel)
        _operations.value = opModel
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
                            fecha = Utils.dateFormat(op.fecha)
                            date = op.fecha
                        }
                        lstOpModel.add(operationModel)
                    }
                }
            }
        }
        return lstOpModel
    }

}