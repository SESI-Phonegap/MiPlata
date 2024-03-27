package com.sesi.miplata.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sesi.miplata.data.entity.Operaciones

@Dao
interface OperationsV2Dao {
    @Insert
    fun insert(operations: Operaciones)

    @Update
    fun update(operations: Operaciones)

    @Delete
    fun delete(operations: Operaciones)

    @Query("SELECT * FROM OPERACIONES WHERE op_fecha BETWEEN :initDate AND :endDate ORDER BY op_fecha ASC")
    fun getOperationsByDate(initDate: Long, endDate: Long): LiveData<List<Operaciones>>

    @Query("SELECT * FROM OPERACIONES")
    fun getAll(): List<Operaciones>
}