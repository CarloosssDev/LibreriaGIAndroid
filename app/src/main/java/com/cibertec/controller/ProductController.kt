package com.cibertec.controller

import android.content.Context
import com.cibertec.controller.api.RetrofitClient
import com.cibertec.model.Category
import com.cibertec.model.Product
import com.cibertec.model.ProductoRequest
import com.cibertec.model.ProductoResponse
import com.cibertec.model.db.AppDatabase
import com.cibertec.model.repository.CategoryRepository
import com.cibertec.model.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductController(context: Context) {
    private val productRepository: ProductRepository
    private val categoryRepository: CategoryRepository
    private val api = RetrofitClient.instance


    init {
        val db = AppDatabase.getDatabase(context)
        productRepository = ProductRepository(db.productDao())
        categoryRepository = CategoryRepository(db.categoryDao())
    }

    fun loadProductosAPI(
        onStart: () -> Unit,
        onFinish: (List<ProductoResponse>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) { onStart() }
                delay(1000)

                withContext(Dispatchers.Main) {
                    api.getProductos().enqueue(object : Callback<List<ProductoResponse>> {
                        override fun onResponse(
                            call: Call<List<ProductoResponse>>,
                            response: Response<List<ProductoResponse>>
                        ) {
                            if (response.isSuccessful) {
                                val productos = response.body()
                                if (productos != null) {
                                    onFinish(productos)
                                }
                            } else {
                                onError(Throwable("Error en la respuesta del servidor"))
                            }
                        }

                        override fun onFailure(call: Call<List<ProductoResponse>>, t: Throwable) {
                            onError(t)
                        }
                    })
                }
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun insertProductoAPI(
        producto: ProductoRequest,
        onInserted: (ProductoResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                api.crearProducto(producto).enqueue(object :  Callback<ProductoResponse> {
                    override fun onResponse(
                        call: Call<ProductoResponse>,
                        response: Response<ProductoResponse>
                    ) {
                        if (response.isSuccessful) {
                            val productoCreado = response.body()
                            if (productoCreado != null) {
                                onInserted(productoCreado)
                            }
                        } else {
                            onError(Throwable("Error en la respuesta del servidor"))
                        }
                    }
                    override fun onFailure(call: Call<ProductoResponse>, t: Throwable) {
                        onError(t)
                    }
                })
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun updateProductAPI(
        producto: ProductoResponse,
        onUpdated: (ProductoResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                api.actualizarProducto(producto.id, producto).enqueue(object :  Callback<ProductoResponse> {
                    override fun onResponse(
                        call: Call<ProductoResponse>,
                        response: Response<ProductoResponse>
                    ) {
                        if (response.isSuccessful) {
                            val productoCreado = response.body()
                            if (productoCreado != null) {
                                onUpdated(productoCreado)
                            }
                        } else {
                            onError(Throwable("Error en la respuesta del servidor"))
                        }
                    }
                    override fun onFailure(call: Call<ProductoResponse>, t: Throwable) {
                        onError(t)
                    }
                })
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun loadProductsAndCategories(
        onStart: () -> Unit,
        onFinish: (products: List<Product>, categories: List<Category>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            onStart()
            delay(1000)

            try {
                val productsDeferred = async(Dispatchers.IO) { productRepository.getAll() }
                val categoriesDeferred = async(Dispatchers.IO) { categoryRepository.getAll() }

                val products = productsDeferred.await()
                val categories = categoriesDeferred.await()

                onFinish(products, categories)

            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun deleteProductAPI(
        id: Int,
        onDeleted: () -> Unit = {},
        onError: (Exception) -> Unit = {}
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                api.eliminarProducto(id).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            onDeleted()
                        } else {
                            onError(Exception("Error en la respuesta del servidor"))
                        }
                    }
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        onError(Exception(t))
                    }
                })
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}