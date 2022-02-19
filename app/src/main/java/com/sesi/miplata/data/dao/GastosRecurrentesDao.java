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
    public void insert(GastosRecurrentes gasto);

    @Update
    public int update(GastosRecurrentes gasto);

    @Delete
    public int delete(GastosRecurrentes gasto);

    @Query("SELECT * FROM GASTOS_RECURRENTES")
    public LiveData<List<GastosRecurrentes>> getAll();

    @Query("SELECT * FROM GASTOS_RECURRENTES")
    public List<GastosRecurrentes> getAllMain();

    @Query("SELECT * FROM GASTOS_RECURRENTES WHERE gr_id = :id")
    public GastosRecurrentes getById(Long id);
}
