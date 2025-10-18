package com.cibertec.controller

import android.content.Context
import com.cibertec.model.*
import com.cibertec.model.db.AppDatabase
import com.cibertec.model.repository.IngresoRepository
import com.cibertec.model.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class IngresoController(context: Context) {
    private val productRepository: ProductRepository
    private val ingresoRepository: IngresoRepository

    init {
        val db = AppDatabase.getDatabase(context)
        ingresoRepository = IngresoRepository(db.ingresoDao())
        productRepository = ProductRepository(db.productDao())
    }

    fun loadIngresosAndProducts(
        onStart: () -> Unit,
        onFinish: (ingresos: List<Ingreso>, products: List<Product>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            onStart()
            delay(1000)

            try {
                val ingresosDeferred = async(Dispatchers.IO) { ingresoRepository.getAll() }
                val productsDeferred = async(Dispatchers.IO) { productRepository.getAll() }

                val ingresos = ingresosDeferred.await()
                val products = productsDeferred.await()

                onFinish(ingresos, products)

            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}