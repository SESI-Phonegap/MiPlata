package com.sesi.miplata.view.fragment.bmensual.day

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.sesi.miplata.databinding.FragmentDayBinding
import com.sesi.miplata.util.Operations
import com.sesi.miplata.view.main.adapter.ViewPagerAdapter
import java.util.Date

class DayFragment : Fragment() {

    private lateinit var binding: FragmentDayBinding
    private lateinit var adapter: ViewPagerAdapter
    private val operationsArray = arrayOf(Operations.INCOME.type, Operations.SPENT.type)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDayBinding.inflate(layoutInflater)

        val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable("date", Date::class.java)
        } else {
            arguments?.getSerializable("date")
        }

        //initTabLayout()
        return binding.root
    }

    private fun initTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.pager) {
                tab, position ->
            tab.text = operationsArray[position]
        }.attach()
    }

}