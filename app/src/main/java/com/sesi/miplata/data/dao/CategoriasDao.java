package com.sesi.miplata.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sesi.miplata.data.entity.Categorias;

import java.util.List;

@Dao
public interface CategoriasDao {

    @Query("SELECT * FROM CATEGORIAS WHERE cat_tipo = :tipo")
    public LiveData<List<Categorias>> getCategoriesByType(String tipo);

    @Query("SELECT * FROM CATEGORIAS")
    public List<Categorias> getAll();

    @Insert
    public void insertCategory(Categorias categoria);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(List<Categorias> categoriasList);

    @Update
    public int updateCategory(Categorias categoria);

    @Delete
    public int deleteCategory(Categorias categoria);
}
