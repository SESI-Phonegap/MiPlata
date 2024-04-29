package com.sesi.miplata.view.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sesi.miplata.data.entity.GastosRecurrentesV2
import com.sesi.miplata.data.entity.IngresosRecurrentes
import com.sesi.miplata.model.OperacionesModel
import com.sesi.miplata.util.Operations
import com.sesi.miplata.view.fragment.bmensual.detalle.tab.OperationTabFragment

class ViewPagerAdapter(
    fragment: FragmentManager,
    lifeCycle: Lifecycle,
    private val operations: List<OperacionesModel>,
    private val recurrentIncome: List<IngresosRecurrentes?>?,
    private val recurrentSpent: List<GastosRecurrentesV2?>?,
    private val operationType: Operations
) : FragmentStateAdapter(fragment, lifeCycle) {

    private lateinit var fragment: OperationTabFragment
    override fun getItemCount(): Int {
        return operations.size
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> fragment = OperationTabFragment(operations, operationType, recurrentIncome, null)
            1 -> fragment = OperationTabFragment(operations, operationType, null, recurrentSpent)
        }
        return fragment
    }
}