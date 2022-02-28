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
import com.sesi.miplata.data.entity.GastosRecurrentes;
import com.sesi.miplata.data.entity.IngresosRecurrentes;
import com.sesi.miplata.databinding.ActivityRegistroGastoIngresoBinding;
import com.sesi.miplata.model.OperacionesModel;
import java.util.List;
import java.util.Objects;

public class RegistroGastoIngresoActivity extends AppCompatActivity {

    private ActivityRegistroGastoIngresoBinding binding;
    private boolean isUpdate;
    private RegistroGastoIngresoViewModel viewModel;
    private ArrayAdapter<Categorias> adapter;
    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroGastoIngresoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        loadAds();
        RegistroGastoIngresoViewModel.RegistroGastoIngresoViewModelFactory factory = new RegistroGastoIngresoViewModel.RegistroGastoIngresoViewModelFactory(getApplication());
        viewModel = new ViewModelProvider(this, factory).get(RegistroGastoIngresoViewModel.class);
        OperacionesModel operacion = (OperacionesModel) getIntent().getSerializableExtra("operacion");

        isUpdate = (operacion != null) ? operacion.isUpdate() : false;
        if(isUpdate){
            binding.btnDelete.setVisibility(View.VISIBLE);
        }

        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        binding.setRegistroGastoIngresoActivity(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        String type = binding.spinnerTipo.getSelectedItem().toString();
        viewModel.setFilterType(type);
        viewModel.getCategorias().observe(this, categorias -> {
            adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, categorias);
            binding.spinnerCategorias.setAdapter(adapter);
            if (isUpdate){
                fillUpdateForm(operacion, categorias);
            }
        });


        binding.btnGuardar.setOnClickListener(v -> {
            saveOperation(operacion);
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

        binding.btnDelete.setOnClickListener(v -> confirmDeleteDialog(operacion));
    }

    private void loadAds(){
        adRequest = new AdRequest.Builder().build();
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

    private void loadInterestecialAd(){

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

        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }

    private void confirmDeleteDialog(OperacionesModel operacion){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Registro");
        builder.setMessage("¿Estas seguro que quieres eliminar este registro?");
        builder.setPositiveButton("Aceptar", (dialogInterface, i) -> {
            String operationType = binding.spinnerTipo.getSelectedItem().toString();
            if (operationType.equals("Gasto")){
                GastosRecurrentes gastoDelete = populatedGasto();
                gastoDelete.setId(operacion.getId());
                viewModel.deleteGasto(gastoDelete);
            } else {
                IngresosRecurrentes ingresoDelete = populatedIngreso();
                ingresoDelete.setId(operacion.getId());
                viewModel.deleteIngreso(ingresoDelete);
            }
            loadInterestecialAd();
        });
        builder.setNegativeButton(Html.fromHtml("<font color='#FF0000'>Cancelar</font>"), (dialogInterface, i) -> dialogInterface.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void fillUpdateForm(OperacionesModel operacion, List<Categorias> categorias){
        binding.tvTitle.setText(getString(R.string.lbl_actualiza_registro));
        binding.etName.setText(operacion.getName());
        binding.etNota.setText(operacion.getNota());
        binding.etMonto.setText(String.valueOf(operacion.getMonto()));
        if (operacion.isGasto()){
            binding.spinnerTipo.setSelection(0);
        } else {
            binding.spinnerTipo.setSelection(1);
        }
        int position = 0;
        for (Categorias categoria : categorias){
            if (operacion.getIdCategoria().equals(categoria.getId())){
                position = adapter.getPosition(categoria);
            }
        }
        binding.spinnerCategorias.setSelection(position);
    }

    public void saveOperation(OperacionesModel operacion){
        String operationType = binding.spinnerTipo.getSelectedItem().toString();
        if (isUpdate){
            if (operationType.equals("Gasto")){
                updateGasto(operacion);
            } else {
                updateIngreso(operacion);
            }

        } else {
            if (operationType.equals("Gasto")){
                insertGasto();
            } else {
                insertIngreso();
            }
        }
        loadInterestecialAd();
    }

    private GastosRecurrentes populatedGasto(){
        GastosRecurrentes gasto = new GastosRecurrentes();
        gasto.setNombre(binding.etName.getText().toString());
        gasto.setNota(Objects.requireNonNull(binding.etNota.getText()).toString());
        gasto.setMonto(Double.parseDouble(Objects.requireNonNull(binding.etMonto.getText()).toString()));
        Categorias slectCat = (Categorias) binding.spinnerCategorias.getSelectedItem();
        gasto.setIdCategoria(slectCat.getId());
        return gasto;
    }

    private void insertGasto() {
        Log.i("GUARDAR","Insert Gasto");
        viewModel.insertGasto(populatedGasto());
    }

    private void updateGasto(OperacionesModel gasto){
        GastosRecurrentes gastoUpdate = populatedGasto();
        gastoUpdate.setId(gasto.getId());
        viewModel.updateGasto(gastoUpdate);
    }

    private IngresosRecurrentes populatedIngreso(){
        IngresosRecurrentes ingreso = new IngresosRecurrentes();
        ingreso.setNombre(binding.etName.getText().toString());
        ingreso.setNota(Objects.requireNonNull(binding.etNota.getText()).toString());
        ingreso.setMonto(Double.parseDouble(Objects.requireNonNull(binding.etMonto.getText()).toString()));
        Categorias slectCat = (Categorias) binding.spinnerCategorias.getSelectedItem();
        ingreso.setIdCategoria(slectCat.getId());
        return ingreso;
    }

    private void insertIngreso(){
        Log.i("GUARDAR","Insert Ingreso");
        viewModel.insertIngreso(populatedIngreso());
    }

    private void updateIngreso(OperacionesModel ingreso){
        IngresosRecurrentes ingresoUpdate = populatedIngreso();
        ingresoUpdate.setId(ingreso.getId());
        viewModel.updateIngreso(ingresoUpdate);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}