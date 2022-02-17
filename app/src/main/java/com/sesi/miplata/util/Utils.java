package com.sesi.miplata.util;

import android.graphics.Color;

import com.sesi.miplata.data.entity.GastosRecurrentes;
import com.sesi.miplata.data.entity.IngresosRecurrentes;

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

    public static double getIngresoTotal(List<IngresosRecurrentes> ingresos){
        double total = 0;
        for (IngresosRecurrentes ingreso: ingresos){
            total += ingreso.getMonto();
        }
        return total;
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
}
