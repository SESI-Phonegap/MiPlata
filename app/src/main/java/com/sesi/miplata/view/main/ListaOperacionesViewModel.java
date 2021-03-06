package com.sesi.miplata.view.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.sesi.miplata.data.entity.Categorias;
import com.sesi.miplata.data.entity.GastosRecurrentes;
import com.sesi.miplata.data.entity.IngresosRecurrentes;
import com.sesi.miplata.data.repository.CategoriasRepository;
import com.sesi.miplata.data.repository.GastosRecurrentesRepository;
import com.sesi.miplata.data.repository.IngresosRecurrentesRepository;
import com.sesi.miplata.model.OperacionesModel;

import java.util.ArrayList;
import java.util.List;

public class ListaOperacionesViewModel extends ViewModel {

    private final CategoriasRepository categoriasRepo;
    private final LiveData<List<GastosRecurrentes>> gastos;
    private final LiveData<List<IngresosRecurrentes>> ingresos;
    private List<Categorias> categorias;

    public ListaOperacionesViewModel(Application application) {
        GastosRecurrentesRepository gastosRepo = new GastosRecurrentesRepository(application);
        IngresosRecurrentesRepository ingresosRepo = new IngresosRecurrentesRepository(application);
        categoriasRepo = new CategoriasRepository(application);
        gastos = gastosRepo.getAll();
        ingresos = ingresosRepo.getAll();
        categorias = null;
    }

    public List<OperacionesModel> fillGastos() {
        categorias = categoriasRepo.getAll();
        List<OperacionesModel> lstOperaciones = new ArrayList<>();
        List<GastosRecurrentes> lstGastos = gastos.getValue();
        List<Categorias> lstCategorias = categorias;
        if (lstGastos != null && lstCategorias != null) {
            for (GastosRecurrentes gasto : lstGastos) {
                for (Categorias categoria : lstCategorias) {
                    if (gasto.getIdCategoria().equals(categoria.getId())) {
                        OperacionesModel operacion = new OperacionesModel();
                        operacion.setId(gasto.getId());
                        operacion.setName(gasto.getNombre());
                        operacion.setNota(gasto.getNota());
                        operacion.setMonto(gasto.getMonto());
                        operacion.setIdCategoria(gasto.getIdCategoria());
                        operacion.setIcono(categoria.getIcono());
                        operacion.setCatNombre(categoria.getNombre());
                        operacion.setGasto(true);
                        operacion.setUpdate(true);
                        lstOperaciones.add(operacion);
                    }
                }
            }
        }
        return lstOperaciones;
    }

    public List<OperacionesModel> fillIngresos(){
        categorias = categoriasRepo.getAll();
        List<OperacionesModel> lstOperaciones = new ArrayList<>();
        List<IngresosRecurrentes> lstIngresos = ingresos.getValue();
        List<Categorias> lstCategorias = categorias;
        if (lstIngresos != null && lstCategorias != null){
            for (IngresosRecurrentes ingreso : lstIngresos){
                for (Categorias categoria : lstCategorias){
                    if (ingreso.getIdCategoria().equals(categoria.getId())){
                        OperacionesModel operacion = new OperacionesModel();
                        operacion.setId(ingreso.getId());
                        operacion.setName(ingreso.getNombre());
                        operacion.setNota(ingreso.getNota());
                        operacion.setMonto(ingreso.getMonto());
                        operacion.setIdCategoria(ingreso.getIdCategoria());
                        operacion.setCatNombre(categoria.getNombre());
                        operacion.setGasto(false);
                        operacion.setUpdate(true);
                        lstOperaciones.add(operacion);
                    }
                }
            }
        }
        return lstOperaciones;
    }

    public LiveData<List<GastosRecurrentes>> getGastos() {
        return gastos;
    }

    public LiveData<List<IngresosRecurrentes>> getIngresos() {
        return ingresos;
    }

    public List<Categorias> getCategorias(){
        return categorias;
    }

    public static class ListaOperacionesViewModelFactory implements ViewModelProvider.Factory {
        private final Application application;

        public ListaOperacionesViewModelFactory(Application application) {
            this.application = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ListaOperacionesViewModel.class)) {
                return (T) new ListaOperacionesViewModel(application);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
