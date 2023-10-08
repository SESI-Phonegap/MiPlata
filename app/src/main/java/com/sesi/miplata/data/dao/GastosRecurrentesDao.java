package com.sesi.miplata.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.sesi.miplata.data.entity.GastosRecurrentesV2;

import java.util.List;

@Dao
public interface GastosRecurrentesDao {

    @Insert
    void insert(GastosRecurrentesV2 gasto);

    @Update
    void update(GastosRecurrentesV2 gasto);

    @Delete
    void delete(GastosRecurrentesV2 gasto);

    @Query("SELECT * FROM GASTOS_RECURRENTES_2 ORDER BY gr_monto DESC")
    LiveData<List<GastosRecurrentesV2>> getAll();

    @Query("SELECT * FROM GASTOS_RECURRENTES_2 ORDER BY gr_monto DESC")
    List<GastosRecurrentesV2> getAllMain();

    @Query("SELECT * FROM GASTOS_RECURRENTES_2 WHERE gr_id = :id")
    GastosRecurrentesV2 getById(Long id);

    @Query("SELECT * FROM GASTOS_RECURRENTES_2 WHERE gr_dia_pago = :diaPago")
    List<GastosRecurrentesV2> getByDate(Integer diaPago);
}
