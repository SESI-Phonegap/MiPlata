package com.sesi.miplata.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.sesi.miplata.data.AppDatabase
import com.sesi.miplata.data.dao.IngresosRecurrentesDao
import com.sesi.miplata.data.entity.IngresosRecurrentes
import javax.inject.Inject

class RecurrentIncomeRepository @Inject constructor() {
    private lateinit var db: AppDatabase
    private var dao: IngresosRecurrentesDao? = null

    fun init(context: Context) {
        db = AppDatabase.getInstance(context)
        dao = db.ingresosRecurrentesDao
    }

    fun insert(ingreso: IngresosRecurrentes?) {
        AppDatabase.databaseWriteExecutor.execute {
            dao?.insert(
                ingreso
            )
        }
    }

    fun update(ingreso: IngresosRecurrentes?) {
        AppDatabase.databaseWriteExecutor.execute {
            dao?.update(
                ingreso
            )
        }
    }

    fun delete(ingreso: IngresosRecurrentes?) {
        AppDatabase.databaseWriteExecutor.execute {
            dao?.delete(
                ingreso
            )
        }
    }

    fun getAll(): List<IngresosRecurrentes?>? {
        return dao?.all
    }

    fun getAllMain(): List<IngresosRecurrentes?>? {
        return dao?.allMain
    }

    fun getById(id: Long?): LiveData<IngresosRecurrentes?>? {
        return dao?.getById(id)
    }
}