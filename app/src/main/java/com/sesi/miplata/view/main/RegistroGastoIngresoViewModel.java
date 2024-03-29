package com.sesi.miplata.view.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.sesi.miplata.data.entity.Categorias;
import com.sesi.miplata.data.entity.GastosRecurrentesV2;
import com.sesi.miplata.data.entity.IngresosRecurrentes;
import com.sesi.miplata.data.repository.CategoriasRepository;
import com.sesi.miplata.data.repository.GastosRecurrentesRepository;
import com.sesi.miplata.data.repository.IngresosRecurrentesRepository;
import java.util.List;

public class RegistroGastoIngresoViewModel extends ViewModel {

    private final GastosRecurrentesRepository gastosRepo;
    private final IngresosRecurrentesRepository ingresosRepo;
    private final LiveData<List<Categorias>> categorias;
    private final MutableLiveData<String> filterType = new MutableLiveData<>();

    public RegistroGastoIngresoViewModel(Application application) {
        gastosRepo = new GastosRecurrentesRepository(application);
        ingresosRepo = new IngresosRecurrentesRepository(application);
        CategoriasRepository categoriasRepository = new CategoriasRepository(application);
        categorias = Transformations.switchMap(filterType,
                categoriasRepository::getCategoriesByType);
    }

    public void insertGasto(GastosRecurrentesV2 gasto) {
        gastosRepo.insert(gasto);
    }

    public void updateGasto(GastosRecurrentesV2 gasto) {
        gastosRepo.update(gasto);
    }

    public void deleteGasto(GastosRecurrentesV2 gasto){
        gastosRepo.delete(gasto);
    }

    public void insertIngreso(IngresosRecurrentes ingreso) {
        ingresosRepo.insert(ingreso);
    }

    public void updateIngreso(IngresosRecurrentes ingreso) {
        ingresosRepo.update(ingreso);
    }

    public void deleteIngreso(IngresosRecurrentes ingreso){
        ingresosRepo.delete(ingreso);
    }

    public void setFilterType(String type) {
        filterType.setValue(type);
    }

    public LiveData<List<Categorias>> getCategorias() {
        return categorias;
    }

    public static class RegistroGastoIngresoViewModelFactory implements ViewModelProvider.Factory {

        private final Application application;

        public RegistroGastoIngresoViewModelFactory(Application application) {
            this.application = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(RegistroGastoIngresoViewModel.class)) {
                return (T) new RegistroGastoIngresoViewModel(application);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
