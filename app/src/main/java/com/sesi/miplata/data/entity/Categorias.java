package com.sesi.miplata.data.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "CATEGORIAS")
public class Categorias {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cat_id")
    @NonNull
    private Long id;

    @ColumnInfo(name = "cat_nombre")
    private String nombre;

    @ColumnInfo(name = "cat_icono",defaultValue="0")
    private int icono;

    @ColumnInfo(name = "cat_tipo")
    private String tipoCategoria;

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

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }

    public String getTipoCategoria() {
        return tipoCategoria;
    }

    public void setTipoCategoria(String tipoCategoria) {
        this.tipoCategoria = tipoCategoria;
    }

    @NonNull
    @Override
    public String toString() {
        return nombre;
    }
}
