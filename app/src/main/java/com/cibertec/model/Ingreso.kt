package com.cibertec.model

import androidx.room.*

@Entity(
    tableName = "ingresos",
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class Ingreso {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var fecha: String = ""
    var cantidad: Int = 0
    var comentario: String = ""

    @ColumnInfo(index = true)
    var product_id: Int = 0

    @Ignore
    constructor(id: Int, fecha: String, cantidad: Int, comentario: String, product_id: Int) {
        this.id = id
        this.fecha = fecha
        this.cantidad = cantidad
        this.comentario = comentario
        this.product_id = product_id
    }

    constructor(fecha: String, cantidad: Int, comentario: String, product_id: Int) {
        this.fecha = fecha
        this.cantidad = cantidad
        this.comentario = comentario
        this.product_id = product_id
    }
    companion object {
        fun getIngresos(): List<Ingreso> {
            return listOf(
                Ingreso(1, "2023-06-01", 10, "Ingreso 1", 1),
                Ingreso(2, "2023-06-02", 5, "Ingreso 2", 2),
                Ingreso(3, "2023-06-03", 8, "Ingreso 3", 3),
                Ingreso(4, "2023-06-04", 12, "Ingreso 4", 4),
                Ingreso(5, "2023-06-05", 6, "Ingreso 5", 5),
                Ingreso(6, "2023-06-06", 9, "Ingreso 6", 6),
                Ingreso(7, "2023-06-07", 7, "Ingreso 7", 7),
                Ingreso(8, "2023-06-08", 11, "Ingreso 8", 8),
                Ingreso(9, "2023-06-09", 4, "Ingreso 9", 9),
                Ingreso(10, "2023-06-10", 15, "Ingreso 10", 10)
            )
        }
    }
}