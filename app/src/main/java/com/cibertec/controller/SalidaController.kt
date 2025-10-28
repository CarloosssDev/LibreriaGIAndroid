package com.cibertec.controller

import com.cibertec.controller.api.RetrofitClient
import com.cibertec.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SalidaController {
    private val apiService = RetrofitClient.instance

    fun loadSalidas(
       onStart: () -> Unit,
       onFinish: (List<SalidaResponse>) -> Unit,
       onError: (Throwable) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) { onStart() }
            delay(1000)
            getSalidas(
                onSuccess = {
                    onFinish(it)
                },
                onError = {
                    onError(it)
                }
            )
        }
    }

    fun getSalidas(
        onSuccess: (List<SalidaResponse>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        apiService.getSalidas().enqueue(object : Callback<List<SalidaResponse>> {
            override fun onResponse(
                call: Call<List<SalidaResponse>?>,
                response: Response<List<SalidaResponse>?>
            ) {
                if (response.isSuccessful) {
                    val salidas = response.body()
                    onSuccess(salidas!!)
                }
            }
            override fun onFailure(call: Call<List<SalidaResponse>?>, t: Throwable) {
                onError(t)
            }
        })
    }
    fun insertSalida(
        salida: SalidaRequest,
        onSuccess: (SalidaResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        apiService.crearSalida(salida).enqueue(object : Callback<SalidaResponse> {
            override fun onResponse(
                call: Call<SalidaResponse?>,
                response: Response<SalidaResponse?>
            ) {
                if (response.isSuccessful) {
                    val salidaResponse = response.body()
                    onSuccess(salidaResponse!!)
                }
            }
            override fun onFailure(call: Call<SalidaResponse?>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun updateSalida(
        salida: SalidaResponse,
        onUpdated: (SalidaResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        apiService.actualizarSalida(salida.id, salida).enqueue(object : Callback<SalidaResponse> {
            override fun onResponse(
                call: Call<SalidaResponse?>,
                response: Response<SalidaResponse?>
            ) {
                if (response.isSuccessful) {
                    val salidaResponse = response.body()
                    onUpdated(salidaResponse!!)
                }
            }
            override fun onFailure(call: Call<SalidaResponse?>, t: Throwable) {
                onError(t)
            }
        })
    }

    fun deleteSalida(
        id: Int,
        onDeleted: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        apiService.eliminarSalida(id).enqueue(object : Callback<Void> {
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