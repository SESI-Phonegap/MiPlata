package com.sesi.miplata.view.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.sesi.miplata.data.repository.OperacionesRepository;

public class RegistroOperacionesMensualesViewModel extends ViewModel {

    private OperacionesRepository operacionesRepo;

    public RegistroOperacionesMensualesViewModel(Application application){
        this.operacionesRepo = new OperacionesRepository(application);
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
