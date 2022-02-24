package com.sesi.miplata.view.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.sesi.miplata.data.entity.Categorias;
import com.sesi.miplata.data.entity.Operaciones;
import com.sesi.miplata.data.repository.CategoriasRepository;
import com.sesi.miplata.data.repository.OperacionesRepository;

import java.util.List;

public class RegistroOperacionesMensualesViewModel extends ViewModel {

    private final OperacionesRepository operacionesRepo;
    private final LiveData<List<Categorias>> categorias;
    private final MutableLiveData<String> filterType = new MutableLiveData<>();


    public RegistroOperacionesMensualesViewModel(Application application){
        this.operacionesRepo = new OperacionesRepository(application);
        CategoriasRepository categoriasRepository = new CategoriasRepository(application);
        categorias = Transformations.switchMap(filterType,
                categoriasRepository::getCategoriesByType);
    }

    public void insertOperacion(Operaciones operacion){
        operacionesRepo.insert(operacion);
    }

    public void updateOperacion(Operaciones operacion){
        operacionesRepo.update(operacion);
    }

    public void deleteOperacion(Operaciones operacion){
        operacionesRepo.delete(operacion);
    }

    public void setFilterType(String type) {
        filterType.setValue(type);
    }

    public LiveData<List<Categorias>> getCategorias() {
        return categorias;
    }

    public static class RegistroOperacionesMensualesViewModelFactory implements ViewModelProvider.Factory{
        private final Application application;

        public RegistroOperacionesMensualesViewModelFactory(Application application){
            this.application = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(RegistroOperacionesMensualesViewModel.class)){
                return (T) new RegistroOperacionesMensualesViewModel(application);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
