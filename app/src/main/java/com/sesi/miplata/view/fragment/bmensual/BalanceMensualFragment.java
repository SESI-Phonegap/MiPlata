package com.sesi.miplata.view.fragment.bmensual;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.sesi.miplata.databinding.FragmentBalanceMensualBinding;
import com.sesi.miplata.util.Utils;
import com.sesi.miplata.view.fragment.datepicker.DatePickerFragment;
import com.sesi.miplata.view.main.ListaOperacionesMensualesActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class BalanceMensualFragment extends Fragment implements OnChartValueSelectedListener {

    private FragmentBalanceMensualBinding binding;
    private BalanceMensualViewModel viewModel;
    private String mes;
    private String ano;
    private AlertDialog dialog;
    private String fechaRango;
    private Long fechaIni;
    private String[] dates;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        BalanceMensualViewModel.BalanceMensualViewModelFactory factory = new BalanceMensualViewModel.BalanceMensualViewModelFactory(requireActivity().getApplication());
        viewModel = new ViewModelProvider(this, factory).get(BalanceMensualViewModel.class);
        binding = FragmentBalanceMensualBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        binding.setBalanceMensualFragment(this);

        fechaIni = null;
        dates = new String[6];
        Calendar calendar = Calendar.getInstance();
        mes = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        ano = String.valueOf(calendar.get(Calendar.YEAR));
        String dateDisplay = Utils.getMonth(mes) + " "+ ano;
        binding.tvTitle.setText(dateDisplay);
        viewModel.setFilterDate(mes, ano);

        viewModel.getGastos().observe(getViewLifecycleOwner(), gastosRecurrentes -> updateUi());

        viewModel.getIngresos().observe(getViewLifecycleOwner(), ingresosRecurrentes -> updateUi());

        viewModel.getOperacionesByMonth().observe(getViewLifecycleOwner(), operaciones -> updateUi());

        configChart();

        binding.constraintIngreso.setOnClickListener(v -> openList(false));

        binding.constraintGastos.setOnClickListener(v -> openList(true));
        binding.btnMesAnterior.setOnClickListener(v -> mesAnterior());
        binding.btnMesSiguiente.setOnClickListener(v -> mesSiguiente());
        binding.tvTitle.setOnClickListener(v -> showDateDialog());
        return binding.getRoot();
    }

    private void mesAnterior() {
        binding.tvTitle.setTextSize(25);
        if (mes.equals("1")) {
            mes = "12";
            int ano = Integer.parseInt(this.ano) - 1;
            this.ano = String.valueOf(ano);
        } else {
            int mes = Integer.parseInt(this.mes) - 1;
            this.mes = String.valueOf(mes);
        }
        Log.i("MesAnterior","mes: " + this.mes +" año: "+ this.ano);
        String dateDisplay = Utils.getMonth(this.mes) + " "+ this.ano;
        binding.tvTitle.setText(dateDisplay);
        viewModel.setFilterDate(this.mes, this.ano);
    }

    private void mesSiguiente() {
        binding.tvTitle.setTextSize(25);
        if (mes.equals("12")){
            mes = "1";
            int ano = Integer.parseInt(this.ano) + 1;
            this.ano = String.valueOf(ano);
        } else {
            int mes = Integer.parseInt(this.mes) + 1;
            this.mes = String.valueOf(mes);
        }
        Log.i("MesSiguiente","mes: " + this.mes +" año: "+ this.ano);
        String dateDisplay = Utils.getMonth(this.mes) + " "+ this.ano;
        binding.tvTitle.setText(dateDisplay);
        viewModel.setFilterDate(this.mes, this.ano);
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

    public void showDateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = requireActivity().getLayoutInflater().inflate(R.layout.dialog_rango_fechas, null);
        EditText etFechaIni = view.findViewById(R.id.et_fecha_ini);
        EditText etFechaFin = view.findViewById(R.id.et_fecha_fin);
        TextView btnCancel = view.findViewById(R.id.btn_cancelar);
        TextView btnBuscar = view.findViewById(R.id.btn_buscar);

        etFechaIni.setOnClickListener(this::showDatePicker);

        etFechaFin.setOnClickListener(this::showDatePickerFin);


        btnBuscar.setOnClickListener(v -> {
            if (etFechaIni.getText().toString().equals("") ||
                    etFechaFin.getText().toString().equals("")){
                Toast.makeText(getContext(), getString(R.string.msg_fechas_required), Toast.LENGTH_SHORT).show();
            } else {
                binding.tvTitle.setText(fechaRango);
                binding.tvTitle.setTextSize(15);
                viewModel.setFilterDateRango(dates[0], dates[1], dates[2],
                        dates[3], dates[4], dates[5]);
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        builder.setView(view);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void showDatePicker(View view) {
        DatePickerFragment datePicker = DatePickerFragment.getInstance(((datePicker1, year, month, day) -> {
            int realMonth = month + 1;
            String selectedDate = day + "/" + realMonth + "/" + year;
            fechaRango = selectedDate;
            EditText etFechaIni = view.findViewById(view.getId());
            etFechaIni.setText(selectedDate);
            Calendar calendar = Calendar.getInstance();
            calendar.set(year,month,day);
            fechaIni = calendar.getTimeInMillis();
            dates[0] = String.valueOf(day);
            dates[1] = String.valueOf(realMonth);
            dates[2] = String.valueOf(year);
        }));
        datePicker.show(requireActivity().getSupportFragmentManager(), "Date Picker");
    }

    private void showDatePickerFin(View view) {
        if (fechaIni != null) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    int realMonth = month + 1;
                    String selectedDate = day + "/" + realMonth + "/" + year;
                    fechaRango += "-" +selectedDate;
                    EditText etFechaFin = view.findViewById(view.getId());
                    etFechaFin.setText(selectedDate);
                    dates[3] = String.valueOf(day);
                    dates[4] = String.valueOf(realMonth);
                    dates[5] = String.valueOf(year);
                }
            }, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(fechaIni);
            datePickerDialog.show();

        } else {
            Toast.makeText(getContext(), getString(R.string.msg_fechaini_required), Toast.LENGTH_SHORT).show();
        }
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