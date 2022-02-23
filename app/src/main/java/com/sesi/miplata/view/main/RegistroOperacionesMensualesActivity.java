package com.sesi.miplata.view.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.sesi.miplata.R;
import com.sesi.miplata.data.entity.Categorias;
import com.sesi.miplata.data.entity.Operaciones;
import com.sesi.miplata.databinding.ActivityRegistroOperacionesMensualesBinding;
import com.sesi.miplata.model.OperacionesModel;
import com.sesi.miplata.util.Utils;
import com.sesi.miplata.view.fragment.datepicker.DatePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RegistroOperacionesMensualesActivity extends AppCompatActivity {

    private ActivityRegistroOperacionesMensualesBinding binding;
    private RegistroOperacionesMensualesViewModel viewModel;
    private ArrayAdapter<Categorias> adapter;
    private boolean isUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroOperacionesMensualesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        RegistroOperacionesMensualesViewModel.RegistroOperacionesMensualesViewModelFactory factory = new RegistroOperacionesMensualesViewModel.RegistroOperacionesMensualesViewModelFactory(getApplication());
        viewModel = new ViewModelProvider(this, factory).get(RegistroOperacionesMensualesViewModel.class);

        OperacionesModel operacion = (OperacionesModel) getIntent().getSerializableExtra("operacion");
        isUpdate = (operacion != null) ? operacion.isUpdate() : false;

        if (isUpdate){
            binding.btnDelete.setVisibility(View.VISIBLE);
        }

        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        binding.setRegistroOperacionesMensualesActivity(this);

        String type = binding.spinnerTipo.getSelectedItem().toString();
        viewModel.setFilterType(type);

        viewModel.getCategorias().observe(this, new Observer<List<Categorias>>() {
            @Override
            public void onChanged(List<Categorias> categorias) {
                adapter = new ArrayAdapter<Categorias>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, categorias);
                binding.spinnerCategorias.setAdapter(adapter);
                if (isUpdate){
                    fillUpdateForm(operacion, categorias);
                }
            }
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
        binding.etDate.setOnClickListener(v -> {
            showDatePicker();
        });

        binding.btnGuardar.setOnClickListener(v -> {
            saveOperation(operacion);
            finish();
        });
    }

    private void showDatePicker(){
        DatePickerFragment datePicker = DatePickerFragment.getInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                int realMonth = month+1;
                final String selectedDate = day + "/" + realMonth + "/" + year;
                binding.etDate.setText(selectedDate);
            }
        });
        datePicker.show(getSupportFragmentManager(), "Date Picker");
    }

    public void fillUpdateForm(OperacionesModel operacion, List<Categorias> categorias){
        binding.tvTitle.setText("Actualiza Registro");
        binding.etNombre.setText(operacion.getName());
        binding.etNota.setText(operacion.getNota());
        binding.etDate.setText(operacion.getFecha());
        binding.etMonto.setText(String.valueOf(operacion.getMonto()));
        if (operacion.isGasto()){
            binding.spinnerTipo.setSelection(0);
        } else {
            binding.spinnerTipo.setSelection(1);
        }
        int position = 0;
        for(Categorias categoria : categorias){
            if (operacion.getIdCategoria().equals(categoria.getId())){
                position = adapter.getPosition(categoria);
            }
        }
        binding.spinnerCategorias.setSelection(position);
    }

    public void saveOperation(OperacionesModel operacion){
        if (isUpdate){
            updateOperacion(operacion);
        } else {
            insertOperacion();
        }
    }

    private void insertOperacion(){
        viewModel.insertOperacion(populateOperation());
    }

    private void updateOperacion(OperacionesModel operacion){
        Operaciones operacionUpdate = populateOperation();
        operacionUpdate.setId(operacion.getId());
        viewModel.updateOperacion(operacionUpdate);
    }

    private Operaciones populateOperation(){
        Operaciones operacion = new Operaciones();
        operacion.setNombre(binding.etNombre.getText().toString());
        operacion.setNota(binding.etNota.getText().toString());
        operacion.setMonto(Double.parseDouble(binding.etMonto.getText().toString()));
        operacion.setTipoOperacion(binding.spinnerTipo.getSelectedItem().toString());
        Categorias selectCat = (Categorias) binding.spinnerCategorias.getSelectedItem();
        operacion.setIdCategoria(selectCat.getId());
        String date = binding.etDate.getText().toString();
        String[] splitDate = date.split("\\/");
        String dia = splitDate[0];
        String mes = splitDate[1];
        String ano = splitDate[2];
        mes = Utils.formatMonth(mes);
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(ano),Integer.parseInt(mes)-1, Integer.parseInt(dia));
        operacion.setFecha(c.getTime());
        return operacion;
    }
}