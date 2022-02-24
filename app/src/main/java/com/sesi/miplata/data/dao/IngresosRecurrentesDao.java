package com.sesi.miplata.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.sesi.miplata.data.entity.IngresosRecurrentes;

import java.util.List;

@Dao
public interface IngresosRecurrentesDao {

    @Insert
    void insert(IngresosRecurrentes ingreso);

    @Update
    void update(IngresosRecurrentes ingreso);

    @Delete
    void delete(IngresosRecurrentes ingreso);

    @Query("SELECT * FROM INGRESOS_RECURRENTES")
    LiveData<List<IngresosRecurrentes>> getAll();

    @Query("SELECT * FROM INGRESOS_RECURRENTES")
    List<IngresosRecurrentes> getAllMain();

    @Query("SELECT * FROM INGRESOS_RECURRENTES WHERE ir_id = :id")
    LiveData<IngresosRecurrentes> getById(Long id);
}
