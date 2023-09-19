package com.sesi.miplata.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.sesi.miplata.data.entity.GastosRecurrentes;

import java.util.List;

@Dao
public interface GastosRecurrentesDao {

    @Insert
    void insert(GastosRecurrentes gasto);

    @Update
    void update(GastosRecurrentes gasto);

    @Delete
    void delete(GastosRecurrentes gasto);

    @Query("SELECT * FROM GASTOS_RECURRENTES ORDER BY gr_monto DESC")
    LiveData<List<GastosRecurrentes>> getAll();

    @Query("SELECT * FROM GASTOS_RECURRENTES ORDER BY gr_monto DESC")
    List<GastosRecurrentes> getAllMain();

    @Query("SELECT * FROM GASTOS_RECURRENTES WHERE gr_id = :id")
    GastosRecurrentes getById(Long id);

    @Query("SELECT * FROM GASTOS_RECURRENTES WHERE gr_dia_pago = :diaPago")
    List<GastosRecurrentes> getByDate(Integer diaPago);
}
