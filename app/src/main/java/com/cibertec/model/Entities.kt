package com.cibertec.model

data class CategoriaResponse(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val productos: List<ProductoResponse>
)
data class CategoriaRequest(
    val nombre: String,
    val descripcion: String
)
data class ProductoResponse(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio_unitario: Double,
    val stock_actual: Int,
    val categoria_id: Int,
    val categoria_nombre: String
)
data class ProductoRequest(
    val nombre: String,
    val descripcion: String,
    val precio_unitario: Double,
    val stock_actual: Int,
    val categoria_id: Int
)

data class IngresoResponse(
    val id: Int,
    val fecha: String,
    val cantidad: Int,
    val comentario: String,
    val producto_id: Int,
    val producto_nombre: String,
)
data class IngresoRequest(
    val cantidad: Int,
    val comentario: String,
    val producto_id: Int
)