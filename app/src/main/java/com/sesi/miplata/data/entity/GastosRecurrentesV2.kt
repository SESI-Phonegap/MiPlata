package com.sesi.miplata.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "GASTOS_RECURRENTES_2",
    foreignKeys = [ForeignKey(
        entity = Categorias::class,
        parentColumns = arrayOf("cat_id"),
        childColumns = arrayOf("gr_id_categoria")
    )],
    indices = [Index("gr_id_categoria")]
)
data class GastosRecurrentesV2(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "gr_id")
    var id: Long?=null,

    @ColumnInfo(name = "gr_nombre")
    var nombre: String? = "",

    @ColumnInfo(name = "gr_nota")
    var nota: String? = "",

    @ColumnInfo(name = "gr_monto", defaultValue = "0")
    var monto: Double = 0.0,

    @ColumnInfo(name = "gr_id_categoria")
    var idCategoria: Long? = 0,

    @ColumnInfo(name = "gr_dia_pago", defaultValue = "0")
    var diaPago: Int? = 0
)