package com.sesi.miplata.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.sesi.miplata.data.AppDatabase
import com.sesi.miplata.data.dao.OperationsV2Dao
import com.sesi.miplata.data.entity.Operaciones
import javax.inject.Inject

class OperationsV2Repository @Inject constructor() {

    private lateinit var db: AppDatabase
    private var dao: OperationsV2Dao? = null
    fun init(context: Context) {
        db = AppDatabase.getInstance(context)
        dao = db.operationsDao
    }

    fun insert(operation: Operaciones) {
        AppDatabase.databaseWriteExecutor.execute {
            dao?.insert(operation)
        }
    }

    fun update(operation: Operaciones) {
        AppDatabase.databaseWriteExecutor.execute { dao?.update(operation) }
    }

    fun delete(operation: Operaciones) {
        AppDatabase.databaseWriteExecutor.execute { dao?.delete(operation) }
    }

    fun getOperationsByDate(initDate: Long, endDate: Long): LiveData<List<Operaciones>>? {
        return dao?.getOperationsByDate(initDate, endDate)
    }

    fun getAll(): List<Operaciones?>? {
        return dao?.getAll()
    }
}