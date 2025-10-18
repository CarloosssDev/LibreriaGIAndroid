package com.cibertec.model

import androidx.room.*

@Entity(
    tableName = "salidas",
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class Salida {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var fecha: String = ""
    var cantidad: Int = 0
    var motivo: String = ""

    @ColumnInfo(index = true)
    var product_id: Int = 0

    @Ignore
    constructor(id: Int, fecha: String, cantidad: Int, motivo: String, product_id: Int) {
        this.id = id
        this.fecha = fecha
        this.cantidad = cantidad
        this.motivo = motivo
        this.product_id = product_id
    }

    constructor(fecha: String, cantidad: Int, motivo: String, product_id: Int) {
        this.fecha = fecha
        this.cantidad = cantidad
        this.motivo = motivo
        this.product_id = product_id
    }
    companion object {
        fun getSalidas(): List<Salida> {
            return listOf(
                Salida(1, "2022-01-01", 10, "Motivo 1", 1),
                Salida(2, "2022-01-02", 20, "Motivo 2", 2),
                Salida(3, "2022-01-03", 30, "Motivo 3", 3),
                Salida(4, "2022-01-04", 40, "Motivo 4", 4),
                Salida(5, "2022-01-05", 50, "Motivo 5", 5)
            )
        }
    }
}