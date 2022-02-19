package com.sesi.miplata.util;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.github.mikephil.charting.utils.ColorTemplate;
import com.sesi.miplata.data.entity.GastosRecurrentes;
import com.sesi.miplata.data.entity.IngresosRecurrentes;
import com.sesi.miplata.data.entity.Operaciones;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static double getGastoTotal(List<GastosRecurrentes> gastos) {
        double total = 0;
        for (GastosRecurrentes gasto : gastos) {
            total += gasto.getMonto();
        }
        return total;
    }

    public static double getGastoMesTotal(List<Operaciones> gastos){
        double total = 0;
        for (Operaciones gasto : gastos) {
            total += gasto.getMonto();
        }
        return total;
    }

    public static double getIngresoTotal(List<IngresosRecurrentes> ingresos){
        double total = 0;
        for (IngresosRecurrentes ingreso: ingresos){
            total += ingreso.getMonto();
        }
        return total;
    }

    public static double getIngresoMesTotal(List<Operaciones> ingresos){
        double total  = 0;
        for (Operaciones ingreso : ingresos){
            total += ingreso.getMonto();
        }
        return total;
    }

    public static SpannableString generateCenterSpannableText(String ingresoTotal) {

        SpannableString s = new SpannableString(ingresoTotal + "\nIngreso Bruto");
        s.setSpan(new RelativeSizeSpan(2.0f), 0, s.length()-13, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 13, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.5f),s.length()-13,s.length(),0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 13, s.length(), 0);
        return s;
    }

    public static List<Integer> getColors(){
        ArrayList<Integer> colors = new ArrayList<>();
        //green
        colors.add(Color.rgb(0,112,26));
        //red
        colors.add(Color.rgb(255,0,0));
        //Rosa
        colors.add(Color.rgb(255,14,127));
        //Morado
        colors.add(Color.rgb(153,0,153));
        //azul
        colors.add(Color.rgb(127,0,255));
        //verde agua
        colors.add(Color.rgb(0,153,153));
        //Mostaza
        colors.add(Color.rgb(204,204,0));
        //naranja
        colors.add(Color.rgb(255,137,0));
        colors.add(Color.rgb(12,232,12));
        colors.add(Color.rgb(0,239,255));
        return colors;
    }

    public static String getCurrencyFormatter(double monto){
        Locale locale = new Locale("en", "US");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        return currencyFormatter.format(monto);
    }

    public static List<Operaciones> getOperationsByType(List<Operaciones> operaciones, String type){
        List<Operaciones> filterOperations = new ArrayList<>();
        for (Operaciones operacion : operaciones){
            if (operacion.getTipoOperacion().equals(type)){
                filterOperations.add(operacion);
            }
        }
        return filterOperations;
    }

    public static String getMonth(int month){
        String mes = "";
        switch (month){
            case 1:
                mes = Month.ENERO.getTextFormat();
                break;
            case 2:
                mes = Month.FEBRERO.getTextFormat();
                break;
            case 3:
                mes = Month.MARZO.getTextFormat();
                break;
            case 4:
                mes= Month.ABRIL.getTextFormat();
                break;
            case 5:
                mes = Month.MAYO.getTextFormat();
                break;
            case 6:
                mes = Month.JUNIO.getTextFormat();
                break;
            case 7:
                mes = Month.JULIO.getTextFormat();
                break;
            case 8:
                mes = Month.AGOSTO.getTextFormat();
                break;
            case 9:
                mes = Month.SEPTIEMBRE.getTextFormat();
                break;
            case 10:
                mes = Month.OCTUBRE.getTextFormat();
                break;
            case 11:
                mes = Month.NOVIEMBRE.getTextFormat();
                break;
            case 12:
                mes = Month.DICIEMBRE.getTextFormat();
                break;
        }
        return mes;
    }
}
