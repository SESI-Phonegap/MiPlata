package com.sesi.miplata.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.sesi.miplata.data.entity.Operaciones;

import java.util.List;

@Dao
public interface OperacionesDao {

    @Insert
    public void insert(Operaciones operacion);

    @Update
    public int update(Operaciones operacion);

    @Delete
    public int delete(Operaciones operacion);

    @Query("SELECT * FROM OPERACIONES WHERE date(op_fecha) BETWEEN date(:fechaIni) AND date(:fechaFinal)")
    public LiveData<List<Operaciones>> getOperationsByDate(String fechaIni, String fechaFinal);
}
