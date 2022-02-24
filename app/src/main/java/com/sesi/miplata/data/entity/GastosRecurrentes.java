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
@Entity(tableName = "GASTOS_RECURRENTES",
        foreignKeys = {@ForeignKey(entity = Categorias.class, parentColumns = "cat_id", childColumns = "gr_id_categoria")},
        indices = {@Index("gr_id_categoria")})
public class GastosRecurrentes {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "gr_id")
    @NonNull
    private Long id;

    @ColumnInfo(name = "gr_nombre")
    private String nombre;

    @ColumnInfo(name = "gr_nota")
    private String nota;

    @ColumnInfo(name = "gr_monto",defaultValue = "0")
    private double monto;

    @ColumnInfo(name = "gr_id_categoria")
    @NonNull
    private Long idCategoria;

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
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

    @NonNull
    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(@NonNull Long idCategoria) {
        this.idCategoria = idCategoria;
    }
}
