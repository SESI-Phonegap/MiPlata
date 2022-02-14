package com.sesi.miplata.view.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.sesi.miplata.R;
import com.sesi.miplata.data.entity.Categorias;
import com.sesi.miplata.data.entity.GastosRecurrentes;
import com.sesi.miplata.data.entity.IngresosRecurrentes;
import com.sesi.miplata.databinding.ActivityListaOperacionesBinding;
import com.sesi.miplata.view.main.adapter.GastosAdapter;

import java.util.List;

public class ListaOperacionesActivity extends AppCompatActivity {

    private ActivityListaOperacionesBinding binding;
    private ListaOperacionesViewModel viewModel;
    private boolean isGastosView;
    private GastosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaOperacionesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ListaOperacionesViewModel.ListaOperacionesViewModelFactory factory = new ListaOperacionesViewModel.ListaOperacionesViewModelFactory(getApplication());
        viewModel = new ViewModelProvider(this, factory).get(ListaOperacionesViewModel.class);
        isGastosView = getIntent().getBooleanExtra("isGastoView", false);
        Log.i("VIEW:", "Vista:" + isGastosView);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        binding.setListaOperacionesActivity(this);

        viewModel.getGastos().observe(this, new Observer<List<GastosRecurrentes>>() {
            @Override
            public void onChanged(List<GastosRecurrentes> gastosRecurrentes) {
                if (isGastosView) {
                    adapter = new GastosAdapter();
                    adapter.setGastos(viewModel.fillGastos());
                    binding.rvList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    binding.rvList.setAdapter(adapter);
                }
            }
        });
        viewModel.getIngresos().observe(this, new Observer<List<IngresosRecurrentes>>() {
            @Override
            public void onChanged(List<IngresosRecurrentes> ingresosRecurrentes) {

            }
        });
    }
}