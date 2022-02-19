package com.sesi.miplata.view.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

import com.sesi.miplata.databinding.ActivityRegistroOperacionesMensualesBinding;
import com.sesi.miplata.view.fragment.datepicker.DatePickerFragment;

public class RegistroOperacionesMensualesActivity extends AppCompatActivity {

    private ActivityRegistroOperacionesMensualesBinding binding;
    private RegistroOperacionesMensualesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroOperacionesMensualesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        RegistroOperacionesMensualesViewModel.RegistroOperacionesMensualesViewModelFactory factory = new RegistroOperacionesMensualesViewModel.RegistroOperacionesMensualesViewModelFactory(getApplication());
        viewModel = new ViewModelProvider(this, factory).get(RegistroOperacionesMensualesViewModel.class);

        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        binding.setRegistroOperacionesMensualesActivity(this);

        binding.etDate.setOnClickListener(v -> {
            showDatePicker();
        });
    }

    private void showDatePicker(){
        DatePickerFragment datePicker = DatePickerFragment.getInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                final String selectedDate = year + "-" + month + "-" + day;
                binding.etDate.setText(selectedDate);
            }
        });
        datePicker.show(getSupportFragmentManager(), "Date Picker");
    }
}