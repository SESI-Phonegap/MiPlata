package com.sesi.miplata.view.fragment.bmensual.detalle.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
) : Fragment() {

    private lateinit var binding: FragmentOperacionTabBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOperacionTabBinding.inflate(layoutInflater)
        //initCategories()
        //initOperations()
        return binding.root
    }

    private fun initCategories() {
        val categories = operations.distinctBy { operacionesModel -> operacionesModel.idCategoria }
        binding.rvCategoriasFilter.adapter = FilterCategoriesAdapter(categories)
    }

    private fun initOperations() {
        val opAdapter = OperacionesAdapter()
        opAdapter.setOperaciones(operations)
        binding.rvOperaciones.adapter = opAdapter
    }

}