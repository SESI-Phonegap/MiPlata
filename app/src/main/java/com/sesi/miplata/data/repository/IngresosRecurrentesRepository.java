package com.sesi.miplata.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.sesi.miplata.data.AppDatabase;
import com.sesi.miplata.data.dao.IngresosRecurrentesDao;
import com.sesi.miplata.data.entity.IngresosRecurrentes;

import java.util.List;

public class IngresosRecurrentesRepository {

    private final IngresosRecurrentesDao ingresosRecurrentesDao;
    private final LiveData<List<IngresosRecurrentes>> ingresosTodos;

    public IngresosRecurrentesRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        ingresosRecurrentesDao = db.getIngresosRecurrentesDao();
        ingresosTodos = ingresosRecurrentesDao.getAll();
    }

    public void insert(IngresosRecurrentes ingreso) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                ingresosRecurrentesDao.insert(ingreso));
    }

    public void update(IngresosRecurrentes ingreso) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                ingresosRecurrentesDao.update(ingreso));
    }

    public void delete(IngresosRecurrentes ingreso) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                ingresosRecurrentesDao.delete(ingreso));
    }

    public LiveData<List<IngresosRecurrentes>> getAll() {
        return ingresosTodos;
    }

    public List<IngresosRecurrentes> getAllMain(){ return  ingresosRecurrentesDao.getAllMain();}

    public LiveData<IngresosRecurrentes> getById(Long id) {
        return ingresosRecurrentesDao.getById(id);
    }
}
