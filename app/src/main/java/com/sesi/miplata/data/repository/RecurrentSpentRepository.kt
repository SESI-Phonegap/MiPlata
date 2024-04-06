package com.sesi.miplata.data.repository

import android.content.Context
import com.sesi.miplata.data.AppDatabase
import com.sesi.miplata.data.dao.GastosRecurrentesDao
import com.sesi.miplata.data.entity.GastosRecurrentesV2
import javax.inject.Inject

class RecurrentSpentRepository @Inject constructor() {
    private lateinit var db: AppDatabase
    private var dao: GastosRecurrentesDao? = null

    fun init(context: Context) {
        db = AppDatabase.getInstance(context)
        dao = db.gastosRecurrentesDao
    }

    fun insert(gasto: GastosRecurrentesV2?) {
        AppDatabase.databaseWriteExecutor.execute { dao?.insert(gasto) }
    }

    fun update(gasto: GastosRecurrentesV2?) {
        AppDatabase.databaseWriteExecutor.execute { dao?.update(gasto) }
    }

    fun delete(gasto: GastosRecurrentesV2?) {
        AppDatabase.databaseWriteExecutor.execute { dao?.delete(gasto) }
    }

    fun getAll(): List<GastosRecurrentesV2?>? {
        return dao?.all
    }

    fun getByDate(diaPago: Int?): List<GastosRecurrentesV2?>? {
        return dao?.getByDate(diaPago)
    }

    fun getAllMain(): List<GastosRecurrentesV2?>? {
        return dao?.allMain
    }

    fun getById(id: Long?): GastosRecurrentesV2? {
        return dao?.getById(id)
    }
}