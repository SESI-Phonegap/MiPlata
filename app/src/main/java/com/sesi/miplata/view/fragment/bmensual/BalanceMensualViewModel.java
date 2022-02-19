package com.sesi.miplata.view.fragment.bmensual;

import android.app.Application;
import android.util.Log;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class BalanceMensualViewModel extends ViewModel {

    private OperacionesRepository operacionesRepo;
    private GastosRecurrentesRepository gastosRepo;
    private IngresosRecurrentesRepository ingresosRepo;
    private LiveData<List<Operaciones>> operaciones;
    private LiveData<List<GastosRecurrentes>> gastos;
    private LiveData<List<IngresosRecurrentes>> ingresos;
    private MutableLiveData<List<String>> filterDate = new MutableLiveData<>();

    public BalanceMensualViewModel(Application application) {
        operacionesRepo = new OperacionesRepository(application);
        gastosRepo = new GastosRecurrentesRepository(application);
        ingresosRepo = new IngresosRecurrentesRepository(application);
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
        return total;
    }

    public void setFilterDate(String date) {
        String[] splitDate = date.split("\\|");
        String mes = splitDate[0];
        String ano = splitDate[1];
        Calendar calendar = new GregorianCalendar(Integer.parseInt(ano), Integer.parseInt(mes), 1);
        int ultimoDiaMes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        String fechaIni = ano + "-" + mes + "-01";
        String fechaFinal = ano + "-" + mes + "-" + ultimoDiaMes;
        List<String> filter = new ArrayList<>();
        filter.add(fechaIni);
        filter.add(fechaFinal);
        filterDate.setValue(filter);
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
