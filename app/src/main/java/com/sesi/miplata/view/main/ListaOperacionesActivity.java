package com.sesi.miplata.view.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.sesi.miplata.R;
import com.sesi.miplata.data.entity.Categorias;
import com.sesi.miplata.data.entity.GastosRecurrentes;
import com.sesi.miplata.data.entity.IngresosRecurrentes;
import com.sesi.miplata.databinding.ActivityListaOperacionesBinding;
import com.sesi.miplata.model.OperacionesModel;
import com.sesi.miplata.util.Utils;
import com.sesi.miplata.view.main.adapter.GastosAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListaOperacionesActivity extends AppCompatActivity implements OnChartValueSelectedListener {

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
                    List<OperacionesModel> lstOp = viewModel.fillGastos();
                    List<OperacionesModel> groupListOp = groupOperations(lstOp);
                    adapter.setGastos(lstOp);
                    adapter.setItemClickListener((OperacionesModel operacion) -> openEditActivity(operacion));
                    setData(groupListOp);
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

        configChart();
    }

    private void openEditActivity(OperacionesModel operacion){
        Log.i("Click","Click RV");
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
        binding.pieChart.setEntryLabelTextSize(12f);
    }

    private void setData(List<OperacionesModel> operaciones){
        ArrayList<PieEntry> entries = new ArrayList<>();
        double total = getTotal(operaciones);
        for(OperacionesModel operacion : operaciones){
            double porcentaje = (operacion.getMonto() / total) * 100;
            float fPorcent = Float.parseFloat(String.valueOf(porcentaje));
            entries.add(new PieEntry(fPorcent, operacion.getCatNombre(), R.drawable.ic_cash_remove));
        }
        PieDataSet dataSet = new PieDataSet(entries, "Balance de Gastos");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        dataSet.setColors(Utils.getColors());
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(17f);
        data.setValueTextColor(Color.WHITE);
        //data.setValueTypeface(tfLight);
        binding.pieChart.setData(data);
        binding.pieChart.highlightValue(null);
        binding.pieChart.invalidate();
        binding.pieChart.setCenterText(generateCenterSpannableText(String.valueOf(total)));
    }

    private double getTotal(List<OperacionesModel> operaciones){
        double total = 0;
        for (OperacionesModel operacion : operaciones){
            total += operacion.getMonto();
        }
        return total;
    }

    private List<OperacionesModel> groupOperations(List<OperacionesModel> operaciones){
        List<Categorias> categorias = viewModel.getCategorias();
        List<OperacionesModel> filterOperations = new ArrayList<>();
        for(Categorias categoria: categorias){
            double suma = 0;
            for(OperacionesModel operacion :operaciones){
                if(categoria.getId().equals(operacion.getIdCategoria())){
                    suma += operacion.getMonto();
                }
            }
            if(suma > 0){
                OperacionesModel model = new OperacionesModel();
                model.setId(categoria.getId());
                model.setCatNombre(categoria.getNombre());
                model.setMonto(suma);
                filterOperations.add(model);
            }
        }
        return filterOperations;
    }

    private SpannableString generateCenterSpannableText(String total) {

        SpannableString s = new SpannableString(total + "\nTotal");
        s.setSpan(new RelativeSizeSpan(2.0f), 0, s.length()-5, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 5, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.5f),s.length()-5,s.length(),0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 5, s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}