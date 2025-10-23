package com.cibertec.controller

import com.cibertec.controller.api.RetrofitClient
import com.cibertec.model.*
import kotlinx.coroutines.*
import retrofit2.*
class IngresoController {
    private val apiService = RetrofitClient.instance

    fun loadIngresos(
        onStart: () -> Unit,
        onFinish: (List<IngresoResponse>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) { onStart() }
            delay(1000)
            getIngresos(
                onSuccess = {
                    onFinish(it)
                },
                onError = {
                    onError(it)
                }
            )
        }
    }

    fun getIngresos(
        onSuccess: (List<IngresoResponse>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        apiService.getIngresos().enqueue(object : Callback<List<IngresoResponse>> {
            override fun onResponse(
                call: Call<List<IngresoResponse>?>,
                response: Response<List<IngresoResponse>?>
            ) {
                if (response.isSuccessful) {
                    val ingresos = response.body()
                    onSuccess(ingresos!!)
                }
            }

            override fun onFailure(call: Call<List<IngresoResponse>?>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun insertIngreso(
        ingreso: IngresoRequest,
        onSuccess: (IngresoResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        apiService.crearIngreso(ingreso).enqueue(object : Callback<IngresoResponse> {
            override fun onResponse(
                call: Call<IngresoResponse?>,
                response: Response<IngresoResponse?>
            ) {
                if (response.isSuccessful) {
                    val ingresoResponse = response.body()
                    onSuccess(ingresoResponse!!)
                }
            }
            override fun onFailure(call: Call<IngresoResponse?>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun updateIngreso(
        ingreso: IngresoResponse,
        onUpdated: (IngresoResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        apiService.actualizarIngreso(ingreso.id, ingreso).enqueue(object : Callback<IngresoResponse> {
            override fun onResponse(
                call: Call<IngresoResponse?>,
                response: Response<IngresoResponse?>
            ) {
                if (response.isSuccessful) {
                    val ingresoResponse = response.body()
                    onUpdated(ingresoResponse!!)
                }
            }
            override fun onFailure(call: Call<IngresoResponse?>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun deleteIngreso(
        id: Int,
        onDeleted: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        apiService.eliminarIngreso(id).enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void?>,
                response: Response<Void?>
            ) {
                if (response.isSuccessful) {
                    onDeleted()
                }
            }
            override fun onFailure(call: Call<Void?>, t: Throwable) {
                onError(t)
            }
        })
    }
}