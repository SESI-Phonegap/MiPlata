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

    /**
     *
     * @param mes - Mes en formato nemerico 01 Enero.
     * @return List of operations.
     */
   /* @Query("SELECT * FROM OPERACIONES WHERE op_tipo_operacion = :tipo AND strtime('%n',op_fecha) = :mes")
    public LiveData<List<Operaciones>> getOperationsByTypeAndMonth(String tipo, String mes);*/
}
