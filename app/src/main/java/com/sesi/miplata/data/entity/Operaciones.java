package com.sesi.miplata.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "OPERACIONES",
        foreignKeys = {@ForeignKey(entity = Categorias.class, parentColumns = "cat_id", childColumns = "op_id_categoria")},
        indices = {@Index("op_id")})
public class Operaciones {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "op_id")
    @NonNull
    private Long id;

    @ColumnInfo(name = "op_nombre")
    private String nombre;

    @ColumnInfo(name = "op_nota")
    private String nota;

    @ColumnInfo(name = "op_fecha")
    private Date fecha;

    @ColumnInfo(name = "op_monto",defaultValue = "0")
    private double monto;

    @ColumnInfo(name = "op_tipo_operacion")
    private String tipoOperacion;

    @ColumnInfo(name = "op_id_categoria")
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }
}
