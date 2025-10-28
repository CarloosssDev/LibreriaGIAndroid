package com.cibertec.controller

import com.cibertec.controller.api.RetrofitClient
import com.cibertec.model.*
import kotlinx.coroutines.*
import retrofit2.*

class ProductController {
    private val apiService = RetrofitClient.instance

    fun loadProductos(
        onStart: () -> Unit,
        onFinish: (List<ProductoResponse>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) { onStart() }
            delay(1000)

            getProductos(
                onSuccess = {
                    onFinish(it)
                },
                onError = {
                    onError(it)
                }
            )
        }
    }

    fun getProducto(
        id: Int,
        onSuccess: (ProductoResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        apiService.getProducto(id).enqueue(object : Callback<ProductoResponse> {
            override fun onResponse(
                call: Call<ProductoResponse>,
                response: Response<ProductoResponse>
            ) {
                if (response.isSuccessful) {
                    val producto = response.body()
                    onSuccess(producto!!)
                }
            }

            override fun onFailure(call: Call<ProductoResponse>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun getProductos(
        onSuccess: (List<ProductoResponse>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        apiService.getProductos().enqueue(object : Callback<List<ProductoResponse>> {
            override fun onResponse(
                call: Call<List<ProductoResponse>>,
                response: Response<List<ProductoResponse>>
            ) {
                if (response.isSuccessful) {
                    val productos = response.body()
                    onSuccess(productos!!)
                }
            }
            override fun onFailure(call: Call<List<ProductoResponse>>, t: Throwable) {
                onError(t)
            }
        })
    }
    
    fun insertProducto(
        producto: ProductoRequest,
        onInsert: (ProductoResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        apiService.crearProducto(producto).enqueue(object : Callback<ProductoResponse> {
            override fun onResponse(
                call: Call<ProductoResponse>,
                response: Response<ProductoResponse>
            ) {
                if (response.isSuccessful) {
                    val productoCreado = response.body()
                    onInsert(productoCreado!!)
            }
        }
            override fun onFailure(call: Call<ProductoResponse>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun updateProducto(
        producto: ProductoResponse,
        onUpdate: (ProductoResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        apiService.actualizarProducto(producto.id, producto)
            .enqueue(object : Callback<ProductoResponse> {
                override fun onResponse(
                    call: Call<ProductoResponse>,
                    response: Response<ProductoResponse>
                ) {
                    if (response.isSuccessful) {
                        val productoUpdated = response.body()
                        onUpdate(productoUpdated!!)
                    }
                }

                override fun onFailure(call: Call<ProductoResponse>, t: Throwable) {
                    onError(t)
                }
            })
    }

    fun deleteProducto(
        id: Int,
        onDelete: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        apiService.eliminarProducto(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onDelete()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onError(t)
            }
        })
    }
}