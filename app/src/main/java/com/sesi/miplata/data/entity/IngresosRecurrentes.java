package com.sesi.miplata.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "INGRESOS_RECURRENTES",
        foreignKeys = {@ForeignKey(entity = Categorias.class, parentColumns = "cat_id", childColumns = "ir_id_categoria")},
        indices = {@Index("ir_id_categoria")})
public class IngresosRecurrentes {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ir_id")
    @NonNull
    private Long id;

    @ColumnInfo(name = "ir_nombre")
    private String nombre;

    @ColumnInfo(name = "ir_nota")
    private String nota;

    @ColumnInfo(name = "ir_monto",defaultValue = "0")
    private double monto;

    @ColumnInfo(name = "ir_id_categoria")
    private Long idCategoria;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }
}
