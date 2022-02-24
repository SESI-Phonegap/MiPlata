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
import com.sesi.miplata.model.OperacionesModel;
import com.sesi.miplata.util.Utils;
import java.util.ArrayList;
import java.util.List;

public class ListaOperacionesMensualesViewModel extends ViewModel {

    private final OperacionesRepository operacionesRepo;
    private final LiveData<List<Operaciones>> operaciones;
    private final CategoriasRepository categoriasRepo;
    private List<Categorias> categorias;
    private final MutableLiveData<List<Long>> filterDates = new MutableLiveData<>();

    public ListaOperacionesMensualesViewModel(Application application){
        operacionesRepo = new OperacionesRepository(application);
        categoriasRepo = new CategoriasRepository(application);
        categorias = null;
        operaciones = Transformations.switchMap(filterDates,
                date -> operacionesRepo.getOperacionesByDate(date.get(0),date.get(1)));
    }

    public List<OperacionesModel> fillOperaciones(Long fechaIni, Long fechaFin, String tipoOperacion){
        categorias = categoriasRepo.getAll();
        List<Operaciones> lstOperaciones = operaciones.getValue();
        List<OperacionesModel> lstOpModel = new ArrayList<>();
        if (lstOperaciones != null) {
            List<Operaciones> filterList = Utils.getOperationsByType(lstOperaciones, tipoOperacion);
            for (Operaciones op : filterList){
                for (Categorias categoria : categorias){
                    if (op.getIdCategoria().equals(categoria.getId())){
                        OperacionesModel operacion = new OperacionesModel();
                        operacion.setId(op.getId());
                        operacion.setName(op.getNombre());
                        operacion.setNota(op.getNota());
                        operacion.setMonto(op.getMonto());
                        operacion.setFecha(Utils.dateFormat(op.getFecha()));
                        operacion.setIdCategoria(categoria.getId());
                        operacion.setCatNombre(categoria.getNombre());
                        operacion.setGasto(tipoOperacion.equals("Gasto"));
                        operacion.setUpdate(true);
                        lstOpModel.add(operacion);
                    }
                }
            }
        }
        return lstOpModel;
    }

    public void setFilterDates(List<Long> dates){
        filterDates.setValue(dates);
    }

    public List<Categorias> getCategorias(){
        return categorias;
    }

    public LiveData<List<Operaciones>> getOperaciones(){
        return operaciones;
    }

    public static class ListaOperacionesMensualesViewModelFactory implements ViewModelProvider.Factory{

        private  final Application application;

        public ListaOperacionesMensualesViewModelFactory(Application application){
            this.application = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ListaOperacionesMensualesViewModel.class)){
                return (T) new ListaOperacionesMensualesViewModel(application);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
