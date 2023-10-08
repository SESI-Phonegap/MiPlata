package com.sesi.miplata.util;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.github.mikephil.charting.utils.ColorTemplate;
import com.sesi.miplata.data.entity.Categorias;
import com.sesi.miplata.data.entity.GastosRecurrentesV2;
import com.sesi.miplata.data.entity.IngresosRecurrentes;
import com.sesi.miplata.data.entity.Operaciones;
import com.sesi.miplata.model.OperacionesModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static double getGastoTotal(List<GastosRecurrentesV2> gastos) {
        double total = 0;
        for (GastosRecurrentesV2 gasto : gastos) {
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

    public static double getTotalOperaciones(List<OperacionesModel> operaciones){
        double total = 0;
        for (OperacionesModel operacion : operaciones){
            total += operacion.getMonto();
        }
        return total;
    }

    public static SpannableString generateCenterSpannableText(String ingresoTotal) {

        SpannableString s = new SpannableString(ingresoTotal + "\nIngreso Bruto");
        s.setSpan(new RelativeSizeSpan(2.0f), 0, s.length()-13, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 13, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.5f),s.length()-13,s.length(),0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.rgb("FF0000")), 0, s.length() - 14,0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 13, s.length(), 0);
        return s;
    }

    public static SpannableString generateListCenterSpannableText(String total) {

        SpannableString s = new SpannableString(total + "\nTotal");
        s.setSpan(new RelativeSizeSpan(2.0f), 0, s.length()-5, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 5, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.5f),s.length()-5,s.length(),0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.rgb("FF0000")), 0, s.length() - 6,0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 5, s.length(), 0);
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

    public static String getMonth(String month){
        String mes = "";
        switch (month){
            case "1":
                mes = Month.ENERO.getTextFormat();
                break;
            case "2":
                mes = Month.FEBRERO.getTextFormat();
                break;
            case "3":
                mes = Month.MARZO.getTextFormat();
                break;
            case "4":
                mes= Month.ABRIL.getTextFormat();
                break;
            case "5":
                mes = Month.MAYO.getTextFormat();
                break;
            case "6":
                mes = Month.JUNIO.getTextFormat();
                break;
            case "7":
                mes = Month.JULIO.getTextFormat();
                break;
            case "8":
                mes = Month.AGOSTO.getTextFormat();
                break;
            case "9":
                mes = Month.SEPTIEMBRE.getTextFormat();
                break;
            case "10":
                mes = Month.OCTUBRE.getTextFormat();
                break;
            case "11":
                mes = Month.NOVIEMBRE.getTextFormat();
                break;
            case "12":
                mes = Month.DICIEMBRE.getTextFormat();
                break;
        }
        return mes;
    }

    public static String formatMonth(String mes){
        return (mes.length() == 1) ? '0' + mes : mes;
    }

    public static List<Long> getDateInitEnd(String mes, String ano){
        Calendar iniDate = Calendar.getInstance();
        iniDate.set(Integer.parseInt(ano), Integer.parseInt(mes)-1, 1, 0, 0, 0);
        Calendar endDate = Calendar.getInstance();
        endDate.set(Integer.parseInt(ano), Integer.parseInt(mes)-1, iniDate.getActualMaximum(Calendar.DAY_OF_MONTH),23,59,59);
        List<Long> dates = new ArrayList<>();
        dates.add(iniDate.getTimeInMillis());
        dates.add(endDate.getTimeInMillis());
        return dates;
    }

    public static List<Long> getDateInitEnd(String diaIni, String mesIni, String anoIni,
                                            String diaFin, String mesFin, String anoFin){
        Calendar iniDate = Calendar.getInstance();
        iniDate.set(Integer.parseInt(anoIni), Integer.parseInt(mesIni) - 1, Integer.parseInt(diaIni), 0, 0, 0);
        Calendar endDate = Calendar.getInstance();
        endDate.set(Integer.parseInt(anoFin), Integer.parseInt(mesFin) - 1, Integer.parseInt(diaFin), 23, 59, 59);
        List<Long> dates = new ArrayList<>();
        dates.add(iniDate.getTimeInMillis());
        dates.add(endDate.getTimeInMillis());
        return dates;
    }

    public static List<OperacionesModel> groupOperations(List<OperacionesModel> operaciones, List<Categorias> categorias){
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

    public static String dateFormat(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        return simpleDateFormat.format(date);
    }

    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
}
