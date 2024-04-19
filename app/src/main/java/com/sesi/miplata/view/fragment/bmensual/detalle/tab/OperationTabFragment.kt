package com.sesi.miplata.view.fragment.bmensual.detalle.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sesi.miplata.R
import com.sesi.miplata.data.entity.Operaciones
import com.sesi.miplata.util.Operations
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OperationTabFragment(operations: List<Operaciones>, operationType: Operations) : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_operacion_tab, container, false)
    }

}