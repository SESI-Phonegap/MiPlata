package com.sesi.miplata.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sesi.miplata.data.AppDatabase;
import com.sesi.miplata.data.dao.IngresosRecurrentesDao;
import com.sesi.miplata.data.entity.IngresosRecurrentes;

import java.util.List;

public class IngresosRecurrentesRepository {

    private final IngresosRecurrentesDao ingresosRecurrentesDao;

    public IngresosRecurrentesRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        ingresosRecurrentesDao = db.getIngresosRecurrentesDao();
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

    public List<IngresosRecurrentes> getAll() {
        return ingresosRecurrentesDao.getAll();
    }

    public List<IngresosRecurrentes> getAllMain(){ return  ingresosRecurrentesDao.getAllMain();}

    public LiveData<IngresosRecurrentes> getById(Long id) {
        return ingresosRecurrentesDao.getById(id);
    }
}
