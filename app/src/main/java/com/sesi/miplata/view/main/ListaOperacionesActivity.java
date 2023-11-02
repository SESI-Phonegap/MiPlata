package com.sesi.miplata.view.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.sesi.miplata.R;
import com.sesi.miplata.data.entity.Categorias;
import com.sesi.miplata.databinding.ActivityListaOperacionesBinding;
import com.sesi.miplata.model.OperacionesModel;
import com.sesi.miplata.util.Utils;
import com.sesi.miplata.view.main.adapter.OperacionesAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListaOperacionesActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    private ActivityListaOperacionesBinding binding;
    private ListaOperacionesViewModel viewModel;
    private boolean isGastosView;
    private OperacionesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaOperacionesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadAds();
        ListaOperacionesViewModel.ListaOperacionesViewModelFactory factory = new ListaOperacionesViewModel.ListaOperacionesViewModelFactory(getApplication());
        viewModel = new ViewModelProvider(this, factory).get(ListaOperacionesViewModel.class);
        isGastosView = getIntent().getBooleanExtra("isGastoView", false);
        Log.i("VIEW:", "Vista:" + isGastosView);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        binding.setListaOperacionesActivity(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        viewModel.getGastos().observe(this, gastosRecurrentes -> {
            if (isGastosView) {
                adapter = new OperacionesAdapter();
                List<OperacionesModel> lstOp = viewModel.fillGastos();
                List<Categorias> categorias = viewModel.getCategorias();
                List<OperacionesModel> groupListOp = Utils.groupOperations(lstOp, categorias);
                adapter.setOperaciones(lstOp);
                adapter.setItemClickListener(this::openEditActivity);
                setData(groupListOp);
                binding.rvList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                binding.rvList.setAdapter(adapter);
            }
        });
        viewModel.getIngresos().observe(this, ingresosRecurrentes -> {
            if (!isGastosView){
                binding.tvTitle.setText(getString(R.string.lbl_ingreso));
                adapter = new OperacionesAdapter();
                List<OperacionesModel> lstOp = viewModel.fillIngresos();
                List<Categorias> categorias = viewModel.getCategorias();
                List<OperacionesModel> groupListOp = Utils.groupOperations(lstOp, categorias);
                adapter.setOperaciones(lstOp);
                adapter.setItemClickListener(this::openEditActivity);
                setData(groupListOp);
                binding.rvList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                binding.rvList.setAdapter(adapter);
            }
        });

        configChart();
    }
    private void loadAds() {
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adViewBanner.loadAd(adRequest);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void openEditActivity(OperacionesModel operacion){
        Intent intent = new Intent(this, RegistroGastoIngresoActivity.class);
        intent.putExtra("operacion", operacion);
        startActivity(intent);
    }

    private void configChart() {
        binding.pieChart.setUsePercentValues(true);
        binding.pieChart.getDescription().setEnabled(false);
        binding.pieChart.setExtraOffsets(5, 10, 5, 5);
        binding.pieChart.setDragDecelerationFrictionCoef(0.95f);
        //binding.pieChart.setCenterTextTypeface(Typeface.ITALIC);

        binding.pieChart.setDrawHoleEnabled(true);
        binding.pieChart.setHoleColor(Color.WHITE);

        binding.pieChart.setTransparentCircleColor(Color.WHITE);
        binding.pieChart.setTransparentCircleAlpha(110);

        binding.pieChart.setHoleRadius(58f);
        binding.pieChart.setTransparentCircleRadius(61f);

        binding.pieChart.setDrawCenterText(true);
        binding.pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        binding.pieChart.setRotationEnabled(true);
        binding.pieChart.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        binding.pieChart.setOnChartValueSelectedListener(this);
        binding.pieChart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = binding.pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        binding.pieChart.setEntryLabelColor(Color.WHITE);
        //binding.pieChart.setEntryLabelTypeface(tfRegular);
        binding.pieChart.setEntryLabelTextSize(10f);
    }

    private void setData(List<OperacionesModel> operaciones){
        ArrayList<PieEntry> entries = new ArrayList<>();
        double total = Utils.getTotalOperaciones((operaciones));
        for(OperacionesModel operacion : operaciones){
            double porcentaje = (operacion.getMonto() / total) * 100;
            float fPorcent = Float.parseFloat(String.valueOf(porcentaje));
            entries.add(new PieEntry(fPorcent, operacion.getCatNombre(), ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_cash_remove_white)));
        }
        PieDataSet dataSet = new PieDataSet(entries, "Balance");
        dataSet.setDrawIcons(true);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        dataSet.setColors(Utils.getColors());
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE);
        //data.setValueTypeface(tfLight);
        binding.pieChart.setData(data);
        binding.pieChart.highlightValue(null);
        binding.pieChart.invalidate();
        binding.pieChart.setCenterText(Utils.generateListCenterSpannableText(Utils.getCurrencyFormatter(total)));
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}