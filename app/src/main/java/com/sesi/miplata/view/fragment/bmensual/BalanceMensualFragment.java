package com.sesi.miplata.view.fragment.bmensual;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import com.sesi.miplata.R;
import com.sesi.miplata.data.entity.GastosRecurrentes;
import com.sesi.miplata.data.entity.IngresosRecurrentes;
import com.sesi.miplata.data.entity.Operaciones;
import com.sesi.miplata.databinding.FragmentBalanceMensualBinding;
import com.sesi.miplata.util.Utils;
import com.sesi.miplata.view.main.ListaOperacionesMensualesActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BalanceMensualFragment extends Fragment implements OnChartValueSelectedListener {

    private FragmentBalanceMensualBinding binding;
    private BalanceMensualViewModel viewModel;
    private String mes;
    private String ano;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        BalanceMensualViewModel.BalanceMensualViewModelFactory factory = new BalanceMensualViewModel.BalanceMensualViewModelFactory(getActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(BalanceMensualViewModel.class);
        binding = FragmentBalanceMensualBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        binding.setBalanceMensualFragment(this);

        Calendar calendar = Calendar.getInstance();
        mes = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        ano = String.valueOf(calendar.get(Calendar.YEAR));
        String dateDisplay = Utils.getMonth(mes) + " "+ ano;
        binding.tvTitle.setText(dateDisplay);
        viewModel.setFilterDate(mes, ano);

        viewModel.getGastos().observe(getViewLifecycleOwner(), new Observer<List<GastosRecurrentes>>() {
            @Override
            public void onChanged(List<GastosRecurrentes> gastosRecurrentes) {
                updateUi();
            }
        });

        viewModel.getIngresos().observe(getViewLifecycleOwner(), new Observer<List<IngresosRecurrentes>>() {
            @Override
            public void onChanged(List<IngresosRecurrentes> ingresosRecurrentes) {
                updateUi();
            }
        });

        viewModel.getOperacionesByMonth().observe(getViewLifecycleOwner(), new Observer<List<Operaciones>>() {
            @Override
            public void onChanged(List<Operaciones> operaciones) {
                updateUi();
            }
        });

        configChart();

        binding.constraintIngreso.setOnClickListener(v -> {
            openList(false);
        });

        binding.constraintGastos.setOnClickListener(v -> {
            openList(true);
        });
        return binding.getRoot();
    }

    private void openList(boolean isGasto){
        Intent intent = new Intent(getContext(), ListaOperacionesMensualesActivity.class);
        intent.putExtra("isGastoView",isGasto);
        intent.putExtra("dates",(Serializable) Utils.getDateInitEnd(Utils.formatMonth(mes),ano));
        startActivity(intent);
    }

    public void updateUi(){
        double gastoTotal = viewModel.getGastoTotal();
        double ingresoTotal = viewModel.getIngresoTotal();
        double ingresoNeto = ingresoTotal - gastoTotal;
        binding.tvGastos.setText(Utils.getCurrencyFormatter(gastoTotal));
        binding.tvIngreso.setText(Utils.getCurrencyFormatter(ingresoTotal));
        binding.tvIngresoNeto.setText(Utils.getCurrencyFormatter(ingresoNeto));
        setData(ingresoTotal,gastoTotal,ingresoNeto);
        binding.chart1.setCenterText(Utils.generateCenterSpannableText(Utils.getCurrencyFormatter(ingresoTotal)));
    }

    private void setData(double ingreso, double gasto, double ingresoNeto) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        float ingresoBrutoF = Float.parseFloat(String.valueOf(ingreso));
        float gastoF = Float.parseFloat(String.valueOf(gasto));
        float ingresoNetoF = Float.parseFloat(String.valueOf(ingresoNeto));
        float porcentajeIngreso = (ingresoNetoF / ingresoBrutoF) * 100;
        float porcentajeGasto = (gastoF / ingresoBrutoF) * 100;
        //ingreso
        entries.add(new PieEntry(porcentajeIngreso, "Ingreso Neto", R.drawable.ic_cash_check));
        entries.add(new PieEntry(porcentajeGasto, "Gastos", R.drawable.ic_cash_remove));

        PieDataSet dataSet = new PieDataSet(entries, "Balance General");
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
        binding.chart1.setData(data);
        // undo all highlights
        binding.chart1.highlightValues(null);

        binding.chart1.invalidate();
    }


    private void configChart() {
        binding.chart1.setUsePercentValues(true);
        binding.chart1.getDescription().setEnabled(false);
        binding.chart1.setExtraOffsets(5, 10, 5, 5);
        binding.chart1.setDragDecelerationFrictionCoef(0.95f);
        //binding.chart1.setCenterTextTypeface(Typeface.ITALIC);

        binding.chart1.setDrawHoleEnabled(true);
        binding.chart1.setHoleColor(Color.WHITE);

        binding.chart1.setTransparentCircleColor(Color.WHITE);
        binding.chart1.setTransparentCircleAlpha(110);

        binding.chart1.setHoleRadius(58f);
        binding.chart1.setTransparentCircleRadius(61f);

        binding.chart1.setDrawCenterText(true);
        binding.chart1.setRotationAngle(0);
        // enable rotation of the chart by touch
        binding.chart1.setRotationEnabled(true);
        binding.chart1.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        binding.chart1.setOnChartValueSelectedListener(this);
        binding.chart1.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = binding.chart1.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        binding.chart1.setEntryLabelColor(Color.WHITE);
        //binding.chart1.setEntryLabelTypeface(tfRegular);
        binding.chart1.setEntryLabelTextSize(12f);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}