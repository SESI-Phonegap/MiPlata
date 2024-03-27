package com.sesi.miplata.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.sesi.miplata.data.entity.Operaciones;

import java.util.List;


@Deprecated
@Dao
public interface OperacionesDao {

    @Insert
    void insert(Operaciones operacion);

    @Update
    void update(Operaciones operacion);

    @Delete
    void delete(Operaciones operacion);

    @Query("SELECT * FROM OPERACIONES WHERE op_fecha BETWEEN :fechaIni AND :fechaFinal ORDER BY op_fecha ASC")
    LiveData<List<Operaciones>> getOperationsByDate(Long fechaIni, Long fechaFinal);

    @Query("SELECT * FROM OPERACIONES")
    List<Operaciones> getAll();
}
