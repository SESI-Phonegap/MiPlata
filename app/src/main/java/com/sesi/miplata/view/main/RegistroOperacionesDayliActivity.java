package com.sesi.miplata.view.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.sesi.miplata.R;
import com.sesi.miplata.data.entity.Categorias;
import com.sesi.miplata.data.entity.Operaciones;
import com.sesi.miplata.databinding.ActivityRegistroOperacionesMensualesBinding;
import com.sesi.miplata.model.OperacionesModel;
import com.sesi.miplata.util.Utils;
import com.sesi.miplata.view.fragment.datepicker.DatePickerFragment;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class RegistroOperacionesDayliActivity extends AppCompatActivity {

    private ActivityRegistroOperacionesMensualesBinding binding;
    private RegistroOperacionesMensualesViewModel viewModel;
    private ArrayAdapter<Categorias> adapter;
    private boolean isUpdate;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroOperacionesMensualesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        loadAds();
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
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        String type = binding.spinnerTipo.getSelectedItem().toString();
        viewModel.setFilterType(type);

        viewModel.getCategorias().observe(this, categorias -> {
            adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item_spinner, categorias);
            binding.spinnerCategorias.setAdapter(adapter);
            binding.spinnerCategorias.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            if (isUpdate){
                fillUpdateForm(operacion, categorias);
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
        binding.etDate.setOnClickListener(v -> showDatePicker());

        binding.btnGuardar.setOnClickListener(v -> {
            saveOperation(operacion);
        });

        binding.btnDelete.setOnClickListener(v -> confirmDeleteDialog(operacion));
    }

    private void loadAds(){
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adViewBanner.loadAd(adRequest);
        InterstitialAd.load(this, getString(R.string.interesticial), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
            }
        });
    }

    private void loadInterestecialAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    finish();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    finish();
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    mInterstitialAd = null;
                    finish();
                }
            });


            mInterstitialAd.show(this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
            loadAds();
        }
    }

    private void confirmDeleteDialog(OperacionesModel operacion){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Registro");
        builder.setMessage("¿Estas seguro que quieres eliminar este registro?");
        builder.setPositiveButton("Aceptar", (dialogInterface, i) -> {
            Operaciones operacionDelete = populateOperation();
            operacionDelete.setId(operacion.getId());
            viewModel.deleteOperacion(operacionDelete);
            loadInterestecialAd();
        });
        builder.setNegativeButton(Html.fromHtml("<font color='#FF0000'>Cancelar</font>"), (dialogInterface, i) -> dialogInterface.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDatePicker(){
        DatePickerFragment datePicker = DatePickerFragment.getInstance((datePicker1, year, month, day) -> {
            int realMonth = month+1;
            final String selectedDate = day + "/" + realMonth + "/" + year;
            binding.etDate.setText(selectedDate);
        });
        datePicker.show(getSupportFragmentManager(), "Date Picker");
    }

    public void fillUpdateForm(OperacionesModel operacion, List<Categorias> categorias){
        binding.tvTitle.setText(getString(R.string.lbl_actualiza_registro));
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
        loadInterestecialAd();
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
        String[] splitDate = date.split("/");
        String dia = splitDate[0];
        String mes = splitDate[1];
        String ano = splitDate[2];
        mes = Utils.formatMonth(mes);
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(ano),Integer.parseInt(mes)-1, Integer.parseInt(dia));
        operacion.setFecha(c.getTime());
        return operacion;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAds();
    }
}