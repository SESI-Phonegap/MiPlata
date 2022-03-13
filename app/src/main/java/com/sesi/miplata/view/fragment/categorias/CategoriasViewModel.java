package com.sesi.miplata.view.fragment.categorias;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.sesi.miplata.data.entity.Categorias;
import com.sesi.miplata.data.repository.CategoriasRepository;
import com.sesi.miplata.view.fragment.bgeneral.BalanceGeneralViewModel;

import java.util.List;

public class CategoriasViewModel extends ViewModel {
    private final LiveData<List<Categorias>> categoriasGastos;
    private final LiveData<List<Categorias>> categoriasIngresos;
    private final CategoriasRepository repository;

    public CategoriasViewModel(Application application) {
        repository = new CategoriasRepository(application);
        categoriasGastos = repository.getCategoriesByType("Gasto");
        categoriasIngresos = repository.getCategoriesByType("Ingreso");
    }

    public LiveData<List<Categorias>> getCategoriasGastos() {
        return categoriasGastos;
    }

    public LiveData<List<Categorias>> getCategoriasIngresos() {
        return categoriasIngresos;
    }

    public void addCategory(String nombre, String tipo) {
        Categorias categorias = new Categorias();
        categorias.setTipoCategoria(tipo);
        categorias.setNombre(nombre);
        categorias.setIcono(0);
        repository.insert(categorias);
    }

    public void updateCategory(Categorias categoria) {
        repository.update(categoria);
    }

    public static class CategoriasViewModelFactory implements ViewModelProvider.Factory {
        private final Application application;

        public CategoriasViewModelFactory(Application application){
            this.application = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(CategoriasViewModel.class)) {
                return (T) new CategoriasViewModel(application);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
