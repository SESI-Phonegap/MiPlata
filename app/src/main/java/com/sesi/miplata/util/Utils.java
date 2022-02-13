package com.sesi.miplata.util;

import com.sesi.miplata.data.entity.GastosRecurrentes;
import com.sesi.miplata.data.entity.IngresosRecurrentes;

import java.util.List;

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
}
