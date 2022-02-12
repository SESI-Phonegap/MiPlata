package com.sesi.miplata.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.sesi.miplata.data.AppDatabase;
import com.sesi.miplata.data.dao.OperacionesDao;
import com.sesi.miplata.data.entity.Operaciones;

import java.util.List;

public class OperacionesRepository {

    private OperacionesDao operacionesDao;

    OperacionesRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        operacionesDao = db.getOperacionesDao();
    }

    public void insert(Operaciones operacion) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                operacionesDao.insert(operacion));
    }

    public void update(Operaciones operacion) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                operacionesDao.update(operacion));
    }

    public void delete(Operaciones operacion) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                operacionesDao.delete(operacion));
    }

    /**
     *
     * @param type - cadena Gasto/Ingreso.
     * @param month - Mes en formato nemerico 01 Enero.
     * @return
     */
    /*public LiveData<List<Operaciones>> getByTypeAndMonth(String type, String month) {
        return operacionesDao.getOperationsByTypeAndMonth(type, month);
    }*/
}
