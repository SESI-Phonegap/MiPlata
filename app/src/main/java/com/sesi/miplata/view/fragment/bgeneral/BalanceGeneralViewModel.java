package com.sesi.miplata.view.fragment.bgeneral;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.sesi.miplata.data.entity.GastosRecurrentesV2;
import com.sesi.miplata.data.entity.IngresosRecurrentes;
import com.sesi.miplata.data.repository.GastosRecurrentesRepository;
import com.sesi.miplata.data.repository.IngresosRecurrentesRepository;
import com.sesi.miplata.util.Utils;
import java.util.List;

public class BalanceGeneralViewModel extends ViewModel {

    private final MutableLiveData<List<GastosRecurrentesV2>> gastos = new MutableLiveData<>();
    private final MutableLiveData<List<IngresosRecurrentes>> ingresos = new MutableLiveData<>();
    private GastosRecurrentesRepository gastosRepo;
    private IngresosRecurrentesRepository ingresosRepo;

    public BalanceGeneralViewModel(Application application) {
        gastosRepo = new GastosRecurrentesRepository(application);
        ingresosRepo = new IngresosRecurrentesRepository(application);
        gastos.setValue(gastosRepo.getAll());
        ingresos.setValue(ingresosRepo.getAll());
    }

    public double getGastoTotal() {
        double total = 0;
        List<GastosRecurrentesV2> gastos = this.gastos.getValue();
        if (gastos != null) {
            total = Utils.getGastoTotal(gastos);
        }
        return total;
    }

    public void getAllGastos() {
        gastos.setValue(gastosRepo.getAll());
    }

    public void getAllIngresos() {
        ingresos.setValue(ingresosRepo.getAll());
    }

    public double getIngresoTotal() {
        double total = 0;
        List<IngresosRecurrentes> ingresos = this.ingresos.getValue();
        if (ingresos != null) {
            total = Utils.getIngresoTotal(ingresos);
        }
        return total;
    }

    public double getIngresoNeto(){
        double total;
        double gastoTotal = getGastoTotal();
        double ingresoTotal = getIngresoTotal();
        total = ingresoTotal - gastoTotal;
        return total;
    }

    public LiveData<List<GastosRecurrentesV2>> getGastos() {
        return gastos;
    }

    public LiveData<List<IngresosRecurrentes>> getIngresos() {
        return ingresos;
    }

    public static class BalanceGeneralViewModelFactory implements ViewModelProvider.Factory {

        private final Application application;

        public BalanceGeneralViewModelFactory(Application application) {
            this.application = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(BalanceGeneralViewModel.class)) {
                return (T) new BalanceGeneralViewModel(application);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
