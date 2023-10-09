package com.sesi.miplata.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sesi.miplata.data.AppDatabase;
import com.sesi.miplata.data.dao.GastosRecurrentesDao;
import com.sesi.miplata.data.entity.GastosRecurrentesV2;

import java.util.List;

public class GastosRecurrentesRepository {

    private final GastosRecurrentesDao gastosRecurrentesDao;
    private final List<GastosRecurrentesV2> gastosTodos;

    public GastosRecurrentesRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        gastosRecurrentesDao = db.getGastosRecurrentesDao();
        gastosTodos = gastosRecurrentesDao.getAll();
    }

    public void insert(GastosRecurrentesV2 gasto){
        AppDatabase.databaseWriteExecutor.execute(() ->
                gastosRecurrentesDao.insert(gasto));
    }

    public void update(GastosRecurrentesV2 gasto){
        AppDatabase.databaseWriteExecutor.execute(() ->
                gastosRecurrentesDao.update(gasto));
    }

    public void delete(GastosRecurrentesV2 gasto){
        AppDatabase.databaseWriteExecutor.execute(() ->
                gastosRecurrentesDao.delete(gasto));
    }

    public List<GastosRecurrentesV2> getAll(){
        return gastosTodos;
    }

    public List<GastosRecurrentesV2> getByDate(Integer diaPago){
        return gastosRecurrentesDao.getByDate(diaPago);
    }

    public List<GastosRecurrentesV2> getAllMain(){ return gastosRecurrentesDao.getAllMain();}
    public GastosRecurrentesV2 getById(Long id){
        return gastosRecurrentesDao.getById(id);
    }
}
