package com.sesi.miplata.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.sesi.miplata.data.AppDatabase;
import com.sesi.miplata.data.dao.GastosRecurrentesDao;
import com.sesi.miplata.data.entity.GastosRecurrentes;

import java.util.List;

public class GastosRecurrentesRepository {

    private final GastosRecurrentesDao gastosRecurrentesDao;
    private final LiveData<List<GastosRecurrentes>> gastosTodos;

    public GastosRecurrentesRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        gastosRecurrentesDao = db.getGastosRecurrentesDao();
        gastosTodos = gastosRecurrentesDao.getAll();
    }

    public void insert(GastosRecurrentes gasto){
        AppDatabase.databaseWriteExecutor.execute(() ->
                gastosRecurrentesDao.insert(gasto));
    }

    public void update(GastosRecurrentes gasto){
        AppDatabase.databaseWriteExecutor.execute(() ->
                gastosRecurrentesDao.update(gasto));
    }

    public void delete(GastosRecurrentes gasto){
        AppDatabase.databaseWriteExecutor.execute(() ->
                gastosRecurrentesDao.delete(gasto));
    }

    public LiveData<List<GastosRecurrentes>> getAll(){
        return gastosTodos;
    }

    public List<GastosRecurrentes> getByDate(Integer diaPago){
        return gastosRecurrentesDao.getByDate(diaPago);
    }

    public List<GastosRecurrentes> getAllMain(){ return gastosRecurrentesDao.getAllMain();}
    public GastosRecurrentes getById(Long id){
        return gastosRecurrentesDao.getById(id);
    }
}
