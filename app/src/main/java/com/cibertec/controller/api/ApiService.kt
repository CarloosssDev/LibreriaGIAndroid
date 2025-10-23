package com.cibertec.controller.api

import com.cibertec.model.*
import retrofit2.*
import retrofit2.http.*
interface ApiService {
    //Categorias
    @GET("categorias")
    fun getCategorias(): Call<List<CategoriaResponse>>

    @POST("categorias")
    fun crearCategoria(@Body categoria: CategoriaRequest): Call<CategoriaResponse>

    @PUT("categorias/{id}")
    fun actualizarCategoria(@Path("id") id: Int, @Body categoria: CategoriaResponse): Call<CategoriaResponse>

    @DELETE("categorias/{id}")
    fun eliminarCategoria(@Path("id") id: Int): Call<Void>

    //Productos
    @GET("productos")
    fun getProductos(): Call<List<ProductoResponse>>

    @GET("productos/{id}")
    fun getProducto(@Path("id") id: Int): Call<ProductoResponse>

    @POST("productos")
    fun crearProducto(@Body producto: ProductoRequest): Call<ProductoResponse>

    @PUT("productos/{id}")
    fun actualizarProducto(@Path("id") id: Int, @Body producto: ProductoResponse): Call<ProductoResponse>

    @DELETE("productos/{id}")
    fun eliminarProducto(@Path("id") id: Int): Call<Void>

    //Ingresos
    @GET("ingresos")
    fun getIngresos(): Call<List<IngresoResponse>>

    @POST("ingresos")
    fun crearIngreso(@Body ingreso: IngresoRequest): Call<IngresoResponse>

    @PUT("ingresos/{id}")
    fun actualizarIngreso(@Path("id") id: Int, @Body ingreso: IngresoResponse): Call<IngresoResponse>

    @DELETE("ingresos/{id}")
    fun eliminarIngreso(@Path("id") id: Int): Call<Void>

    //Salidas
    @GET("salidas")
    fun getSalidas(): Call<List<SalidaResponse>>

    @POST("salidas")
    fun crearSalida(@Body ingreso: SalidaRequest): Call<SalidaResponse>

    @PUT("salidas/{id}")
    fun actualizarSalida(@Path("id") id: Int, @Body salida: SalidaResponse): Call<SalidaResponse>

    @DELETE("salidas/{id}")
    fun eliminarSalida(@Path("id") id: Int): Call<Void>
}