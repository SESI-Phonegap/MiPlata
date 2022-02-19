package com.sesi.miplata.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.sesi.miplata.data.AppDatabase;
import com.sesi.miplata.data.dao.GastosRecurrentesDao;
import com.sesi.miplata.data.entity.GastosRecurrentes;

import java.util.List;

public class GastosRecurrentesRepository {

    private GastosRecurrentesDao gastosRecurrentesDao;
    private LiveData<List<GastosRecurrentes>> gastosTodos;

    public GastosRecurrentesRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
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

    public List<GastosRecurrentes> getAllMain(){ return gastosRecurrentesDao.getAllMain();}
    public GastosRecurrentes getById(Long id){
        return gastosRecurrentesDao.getById(id);
    }
}
