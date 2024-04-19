package com.sesi.miplata.view.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sesi.miplata.data.entity.Operaciones
import com.sesi.miplata.util.Operations
import com.sesi.miplata.view.fragment.bmensual.detalle.tab.OperationTabFragment

class ViewPagerAdapter(
    fragment: FragmentManager,
    lifeCycle: Lifecycle,
    private val operations: List<Operaciones>,
    private  val operationType: Operations
    ): FragmentStateAdapter(fragment, lifeCycle) {

    private lateinit var fragment: OperationTabFragment
    override fun getItemCount(): Int {
        return operations.size
    }

    override fun createFragment(position: Int): Fragment {
        fragment = OperationTabFragment(operations, operationType)
        return fragment
    }
}