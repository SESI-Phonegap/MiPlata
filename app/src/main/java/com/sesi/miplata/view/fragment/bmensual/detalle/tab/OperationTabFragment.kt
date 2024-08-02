package com.sesi.miplata.view.fragment.bmensual.detalle.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sesi.miplata.data.dto.CategoryDayDto
import com.sesi.miplata.data.entity.GastosRecurrentesV2
import com.sesi.miplata.data.entity.IngresosRecurrentes
import com.sesi.miplata.databinding.FragmentOperacionTabBinding
import com.sesi.miplata.model.OperacionesModel
import com.sesi.miplata.util.Operations
import com.sesi.miplata.view.main.adapter.FilterCategoriesAdapter
import com.sesi.miplata.view.main.adapter.OperacionesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OperationTabFragment(
    private val operations: List<OperacionesModel>,
    private val operationType: Operations,
    private val recurrentIncome: List<IngresosRecurrentes?>?,
    private val recurrentSpent: List<GastosRecurrentesV2?>?
) : Fragment(), CategoryAction {

    private lateinit var binding: FragmentOperacionTabBinding
    private lateinit var opAdapter:OperacionesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        opAdapter = OperacionesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOperacionTabBinding.inflate(layoutInflater)
        binding.tvTitle.text = operationType.type
        initCategories()
        initOperations()
        return binding.root
    }

    private fun initCategories() {
        val categories = operations.distinctBy { operacionesModel -> operacionesModel.idCategoria }
        val categoriesDayDto = mutableListOf<CategoryDayDto>()
        categories.forEach { category ->
            categoriesDayDto.add(CategoryDayDto(category.idCategoria, category.catNombre))
        }
        binding.rvCategoriasFilter.adapter = FilterCategoriesAdapter(categoriesDayDto, this)
    }

    private fun initOperations() {
        opAdapter.setOperaciones(operations)
        binding.rvOperaciones.adapter = opAdapter
    }

    override fun onClickCategory(category: CategoryDayDto) {
        val filteredOperations = operations.filter { it.idCategoria == category.categoryId }
        opAdapter.setOperaciones(filteredOperations)
    }

    override fun onDeselectCategory() {
        opAdapter.setOperaciones(operations)
    }

}

interface CategoryAction {
    fun onClickCategory(category: CategoryDayDto)
    fun onDeselectCategory()
}