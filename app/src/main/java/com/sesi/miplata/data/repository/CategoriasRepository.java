package com.sesi.miplata.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.sesi.miplata.data.AppDatabase;
import com.sesi.miplata.data.dao.CategoriasDao;
import com.sesi.miplata.data.entity.Categorias;
import java.util.List;

@Deprecated
public class CategoriasRepository {

    private final CategoriasDao categoriasDao;
    private final List<Categorias> categories;

    public CategoriasRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        categoriasDao = db.getCategoriasDao();
        categories = categoriasDao.getAll();
    }

    public void insert(Categorias categoria) {
        AppDatabase.databaseWriteExecutor.execute(() -> categoriasDao.insertCategory(categoria));
    }

    public void update(Categorias categoria){
        AppDatabase.databaseWriteExecutor.execute(() ->
                categoriasDao.updateCategory(categoria));
    }

    public void delete(Categorias categoria){
        AppDatabase.databaseWriteExecutor.execute(() ->
                categoriasDao.deleteCategory(categoria));
    }

    public LiveData<List<Categorias>> getCategoriesByType(String type){
        return categoriasDao.getCategoriesByType(type);
    }

    public List<Categorias> getAll(){
        return categories;
    }

}
