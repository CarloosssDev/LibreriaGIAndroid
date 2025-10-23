package com.cibertec.controller

import com.cibertec.controller.api.RetrofitClient
import com.cibertec.model.CategoriaRequest
import com.cibertec.model.CategoriaResponse
import kotlinx.coroutines.*
import retrofit2.*

class CategoryController {
    private val apiService = RetrofitClient.instance
    fun loadCategorias(
        onStart: () -> Unit,
        onFinish: (List<CategoriaResponse>) -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) { onStart() }
            delay(1000)
            getCategorias(
                onSuccess = {
                    onFinish(it)
                },
                onError = {
                    onError(it)
                }
            )
        }
    }
    fun getCategorias(
        onSuccess: (List<CategoriaResponse>) -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        apiService.getCategorias().enqueue(object : Callback<List<CategoriaResponse>> {
            override fun onResponse(
                call: Call<List<CategoriaResponse>>,
                response: Response<List<CategoriaResponse>>
            ) {
                if (response.isSuccessful) {
                    val categorias = response.body()
                    onSuccess(categorias!!)
                }
            }
            override fun onFailure(call: Call<List<CategoriaResponse>>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun insertCategoria(
        categoria: CategoriaRequest,
        onInsert: (CategoriaResponse) -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        apiService.crearCategoria(categoria).enqueue(object : Callback<CategoriaResponse> {
            override fun onResponse(
                call: Call<CategoriaResponse>,
                response: Response<CategoriaResponse>
            ) {
                if (response.isSuccessful) {
                    val categoria = response.body()
                    onInsert(categoria!!)
                }
            }
            override fun onFailure(call: Call<CategoriaResponse>, t: Throwable) {
                onError(t)
            }
        })
    }
    fun updateCategoria(
        categoria: CategoriaResponse,
        onUpdate: (CategoriaResponse) -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        apiService.actualizarCategoria(categoria.id, categoria)
            .enqueue(object : Callback<CategoriaResponse> {
                override fun onResponse(
                    call: Call<CategoriaResponse>,
                    response: Response<CategoriaResponse>
                ) {
                    if (response.isSuccessful) {
                        val categoriaUpdated = response.body()
                        onUpdate(categoriaUpdated!!)
                    }
                }

                override fun onFailure(call: Call<CategoriaResponse>, t: Throwable) {
                    onError(t)
                }
            })
    }

    fun deleteCategoria(
        id: Int,
        onDelete: () -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        apiService.eliminarCategoria(id).enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
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