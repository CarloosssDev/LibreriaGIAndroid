package com.cibertec.controller.api

import com.cibertec.model.CategoriaResponse
import com.cibertec.model.ProductoRequest
import com.cibertec.model.ProductoResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
interface ApiService {
    //Productos
    @GET("productos")
    fun getProductos(): Call<List<ProductoResponse>>

    @POST("productos")
    fun crearProducto(@Body producto: ProductoRequest): Call<ProductoResponse>

    @PUT("productos/{id}")
    fun actualizarProducto(@Path("id") id: Int, @Body producto: ProductoResponse): Call<ProductoResponse>

    @DELETE("productos/{id}")
    fun eliminarProducto(@Path("id") id: Int): Call<Void>


    //Categorias
    @GET("categorias")
    suspend fun getCategorias(): Response<List<CategoriaResponse>>
}