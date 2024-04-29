package com.sesi.miplata.data.repository

import android.content.Context
import com.sesi.miplata.data.AppDatabase
import com.sesi.miplata.data.dao.CategoriasDao
import com.sesi.miplata.data.entity.Categorias
import javax.inject.Inject

class CategoryRepository @Inject constructor() {
    private lateinit var db: AppDatabase
    private var dao: CategoriasDao? = null

    fun init(context: Context) {
        db = AppDatabase.getInstance(context)
        dao = db.categoriasDao
    }

    fun getAll():List<Categorias> {
        return dao?.all ?: arrayListOf()
    }
}