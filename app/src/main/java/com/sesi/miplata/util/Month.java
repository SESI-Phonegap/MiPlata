package com.sesi.miplata.util;

public enum Month {
    ENERO(1,"Enero"),
    FEBRERO(2, "Febrero"),
    MARZO(3, "Marzo"),
    ABRIL(4, "Abril"),
    MAYO(5, "Mayo"),
    JUNIO(6, "Junio"),
    JULIO(7, "Julio"),
    AGOSTO(8, "Agosto"),
    SEPTIEMBRE(9 , "Septiembre"),
    OCTUBRE(10, "Octubre"),
    NOVIEMBRE(11, "Noviembre"),
    DICIEMBRE(12, "Diciembre");

    private final int numberFormat;
    private final String textFormat;

    Month(int numberFormat, String textFormat){
        this.numberFormat = numberFormat;
        this.textFormat = textFormat;
    }
    public int getNumberFormat() {
        return numberFormat;
    }

    public String getTextFormat() {
        return textFormat;
    }
}
