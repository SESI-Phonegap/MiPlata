package com.sesi.miplata.view.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.sesi.miplata.R;
import com.sesi.miplata.data.entity.Categorias;
import com.sesi.miplata.data.entity.GastosRecurrentes;
import com.sesi.miplata.data.entity.IngresosRecurrentes;
import com.sesi.miplata.databinding.ActivityRegistroGastoIngresoBinding;

import java.util.List;

public class RegistroGastoIngresoActivity extends AppCompatActivity {

    private ActivityRegistroGastoIngresoBinding binding;
    private boolean isUpdate;
    private RegistroGastoIngresoViewModel viewModel;
    private ArrayAdapter<Categorias> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroGastoIngresoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        RegistroGastoIngresoViewModel.RegistroGastoIngresoViewModelFactory factory = new RegistroGastoIngresoViewModel.RegistroGastoIngresoViewModelFactory(getApplication());
        viewModel = new ViewModelProvider(this, factory).get(RegistroGastoIngresoViewModel.class);
        isUpdate = getIntent().getBooleanExtra("isUpdate",false);

        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        binding.setRegistroGastoIngresoActivity(this);


        String type = binding.spinnerTipo.getSelectedItem().toString();
        viewModel.setFilterType(type);
        viewModel.getCategorias().observe(this, new Observer<List<Categorias>>() {
            @Override
            public void onChanged(List<Categorias> categorias) {
                adapter = new ArrayAdapter<Categorias>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, categorias);
                binding.spinnerCategorias.setAdapter(adapter);
            }
        });
        binding.btnGuardar.setOnClickListener(v -> {
            saveOperation();
        });

        binding.spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                String selectedType = adapterView.getItemAtPosition(pos).toString();
                viewModel.setFilterType(selectedType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void saveOperation(){
        String operationType = binding.spinnerTipo.getSelectedItem().toString();
        if (isUpdate){
            if (operationType.equals("Gasto")){
                //updateGasto();
            } else {
                //updateIngreso();
            }

        } else {
            if (operationType.equals("Gasto")){
                insertGasto();
            } else {
                insertIngreso();
            }
        }
    }

    private GastosRecurrentes populatedGasto(){
        GastosRecurrentes gasto = new GastosRecurrentes();
        gasto.setNombre(binding.etName.getText().toString());
        gasto.setNota(binding.etNota.getText().toString());
        gasto.setMonto(Double.parseDouble(binding.etMonto.getText().toString()));
        Categorias slectCat = (Categorias) binding.spinnerCategorias.getSelectedItem();
        gasto.setIdCategoria(slectCat.getId());
        return gasto;
    }

    private void insertGasto() {
        Log.i("GUARDAR","Insert Gasto");
        viewModel.insertGasto(populatedGasto());
    }

    private void updateGasto(Long id){
        GastosRecurrentes gasto = populatedGasto();
        gasto.setId(id);
        viewModel.updateGasto(gasto);
    }

    private IngresosRecurrentes populatedIngreso(){
        IngresosRecurrentes ingreso = new IngresosRecurrentes();
        ingreso.setNombre(binding.etName.getText().toString());
        ingreso.setNota(binding.etNota.getText().toString());
        ingreso.setMonto(Double.parseDouble(binding.etMonto.getText().toString()));
        Categorias slectCat = (Categorias) binding.spinnerCategorias.getSelectedItem();
        ingreso.setIdCategoria(slectCat.getId());
        return ingreso;
    }

    private void insertIngreso(){
        Log.i("GUARDAR","Insert Ingreso");
        viewModel.insertIngreso(populatedIngreso());
    }

    private void updateIngreso(Long id){
        IngresosRecurrentes ingreso = populatedIngreso();
        ingreso.setId(id);
        viewModel.updateIngreso(ingreso);
    }
}