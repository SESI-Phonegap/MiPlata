package com.sesi.miplata.view.fragment.bmensual;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.sesi.miplata.data.entity.GastosRecurrentes;
import com.sesi.miplata.data.entity.IngresosRecurrentes;
import com.sesi.miplata.data.entity.Operaciones;
import com.sesi.miplata.data.repository.GastosRecurrentesRepository;
import com.sesi.miplata.data.repository.IngresosRecurrentesRepository;
import com.sesi.miplata.data.repository.OperacionesRepository;
import com.sesi.miplata.util.Utils;
import java.util.List;

public class BalanceMensualViewModel extends ViewModel {

    private final OperacionesRepository operacionesRepo;
    private final LiveData<List<Operaciones>> operaciones;
    private final LiveData<List<GastosRecurrentes>> gastos;
    private final LiveData<List<IngresosRecurrentes>> ingresos;
    private final MutableLiveData<List<Long>> filterDate = new MutableLiveData<>();

    public BalanceMensualViewModel(Application application) {
        operacionesRepo = new OperacionesRepository(application);
        GastosRecurrentesRepository gastosRepo = new GastosRecurrentesRepository(application);
        IngresosRecurrentesRepository ingresosRepo = new IngresosRecurrentesRepository(application);
        gastos = gastosRepo.getAll();
        ingresos = ingresosRepo.getAll();
        operaciones = Transformations.switchMap(filterDate,
                date -> operacionesRepo.getOperacionesByDate(date.get(0), date.get(1)));
    }

    public LiveData<List<Operaciones>> getOperacionesByMonth() {
        return operaciones;
    }

    public double getGastoTotal(){
        return getGastoRecurrentesTotal() + getGastoMesTotal();
    }

    private double getGastoRecurrentesTotal() {
        double total = 0;
        List<GastosRecurrentes> gastos = this.gastos.getValue();
        if (gastos != null) {
            total = Utils.getGastoTotal(gastos);
        }
        return total;
    }

    private double getGastoMesTotal(){
        double total = 0;
        List<Operaciones> operaciones = this.operaciones.getValue();
        if (operaciones != null) {
            List<Operaciones> gastos = Utils.getOperationsByType(operaciones, "Gasto");
            total = Utils.getGastoMesTotal(gastos);
        }
        return total;
    }

    public  double getIngresoTotal(){
        return getIngresosRecurrentesTotal() + getIngresoMesTotal();
    }

    private double getIngresosRecurrentesTotal(){
        double total = 0;
        List<IngresosRecurrentes> ingresos = this.ingresos.getValue();
        if (ingresos != null){
            total = Utils.getIngresoTotal(ingresos);
        }
        return total;
    }

    private double getIngresoMesTotal(){
        double total = 0;
        List<Operaciones> operaciones = this.operaciones.getValue();
        if (operaciones != null){
            List<Operaciones> ingresos = Utils.getOperationsByType(operaciones,"Ingreso");
            total = Utils.getIngresoMesTotal(ingresos);
        }
        List<Operaciones> opAll = operacionesRepo.getAll();
        int size = opAll.size();
        return total;
    }

    public void setFilterDate(String mes, String ano) {
        String formatMes = Utils.formatMonth(mes);
        List<Long> dates = Utils.getDateInitEnd(formatMes,ano);
        filterDate.setValue(dates);
    }

    public LiveData<List<GastosRecurrentes>> getGastos() {
        return gastos;
    }

    public LiveData<List<IngresosRecurrentes>> getIngresos() {
        return ingresos;
    }

    public static class BalanceMensualViewModelFactory implements ViewModelProvider.Factory {

        private final Application application;

        public BalanceMensualViewModelFactory(Application application) {
            this.application = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(BalanceMensualViewModel.class)) {
                return (T) new BalanceMensualViewModel(application);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
