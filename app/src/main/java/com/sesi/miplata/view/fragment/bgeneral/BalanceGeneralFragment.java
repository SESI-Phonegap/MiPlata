package com.sesi.miplata.view.fragment.bgeneral;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.sesi.miplata.databinding.FragmentBalanceGeneralBinding;
import com.sesi.miplata.util.Utils;
import com.sesi.miplata.view.main.ListaOperacionesActivity;
import java.util.ArrayList;
import java.util.Objects;

public class BalanceGeneralFragment extends Fragment implements OnChartValueSelectedListener {

    private FragmentBalanceGeneralBinding binding;
    private BalanceGeneralViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BalanceGeneralViewModel.BalanceGeneralViewModelFactory factory = new BalanceGeneralViewModel.BalanceGeneralViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(BalanceGeneralViewModel.class);

        binding = FragmentBalanceGeneralBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        binding.setBalanceGeneralFragment(this);

        viewModel.getGastos().observe(getViewLifecycleOwner(), gastosRecurrentes -> {
            double gastoTotal = viewModel.getGastoTotal();
            double ingresoTotal = viewModel.getIngresoTotal();
            double ingresoNeto = viewModel.getIngresoNeto();
            binding.tvGastosGeneral.setText(Utils.getCurrencyFormatter(gastoTotal));
            binding.tvIngresoNeto.setText(Utils.getCurrencyFormatter(ingresoNeto));
            setData(ingresoTotal, gastoTotal, ingresoNeto);
        });
        viewModel.getIngresos().observe(getViewLifecycleOwner(), ingresosRecurrentes -> {
            double gastoTotal = viewModel.getGastoTotal();
            double ingresoTotal = viewModel.getIngresoTotal();
            double ingresoNeto = viewModel.getIngresoNeto();
            binding.tvIngresosGeneral.setText(Utils.getCurrencyFormatter(ingresoTotal));
            binding.tvIngresoNeto.setText(Utils.getCurrencyFormatter(ingresoNeto));
            setData(ingresoTotal, gastoTotal, ingresoNeto);
            binding.chartGeneral.setCenterText(Utils.generateCenterSpannableText(Utils.getCurrencyFormatter(ingresoTotal)));
        });

        configChart();

        binding.cosntraintGastosGeneral.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ListaOperacionesActivity.class);
            intent.putExtra("isGastoView",true);
            startActivity(intent);

        });

        binding.cosntraintIngresoGeneral.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ListaOperacionesActivity.class);
            startActivity(intent);
        });
        return binding.getRoot();
    }

    private void setData(double ingreso, double gasto, double ingresoNeto) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        float ingresoBrutoF = Float.parseFloat(String.valueOf(ingreso));
        float gastoF = Float.parseFloat(String.valueOf(gasto));
        float ingresoNetoF = Float.parseFloat(String.valueOf(ingresoNeto));
        float porcentajeIngreso = (ingresoNetoF / ingresoBrutoF) * 100;
        float porcentajeGasto = (gastoF / ingresoBrutoF) * 100;
        if (ingresoNetoF >= 0) {
        //ingreso
        entries.add(new PieEntry(ingresoNetoF, "Ingreso Neto", ContextCompat.getDrawable(requireContext(),R.drawable.ic_cash_check_white)));
        entries.add(new PieEntry(gastoF, "Gastos", ContextCompat.getDrawable(requireContext(),R.drawable.ic_cash_remove_white)));

        PieDataSet dataSet = new PieDataSet(entries, "");
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
            binding.chartGeneral.setData(data);
            // undo all highlights
            binding.chartGeneral.highlightValues(null);

            binding.chartGeneral.invalidate();
        }
    }

    private void configChart() {
        binding.chartGeneral.setUsePercentValues(true);
        binding.chartGeneral.getDescription().setEnabled(false);
        binding.chartGeneral.setExtraOffsets(5, 10, 5, 5);
        binding.chartGeneral.setDragDecelerationFrictionCoef(0.95f);
        //binding.chartGeneral.setCenterTextTypeface(Typeface.ITALIC);

        binding.chartGeneral.setDrawHoleEnabled(true);
        binding.chartGeneral.setHoleColor(Color.WHITE);

        binding.chartGeneral.setTransparentCircleColor(Color.WHITE);
        binding.chartGeneral.setTransparentCircleAlpha(110);

        binding.chartGeneral.setHoleRadius(58f);
        binding.chartGeneral.setTransparentCircleRadius(61f);

        binding.chartGeneral.setDrawCenterText(true);
        binding.chartGeneral.setRotationAngle(0);
        // enable rotation of the chart by touch
        binding.chartGeneral.setRotationEnabled(true);
        binding.chartGeneral.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        binding.chartGeneral.setOnChartValueSelectedListener(this);
        binding.chartGeneral.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = binding.chartGeneral.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        binding.chartGeneral.setEntryLabelColor(Color.WHITE);
        //binding.chartGeneral.setEntryLabelTypeface(tfRegular);
        binding.chartGeneral.setEntryLabelTextSize(10f);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getAllGastos();
        viewModel.getAllIngresos();
    }
}