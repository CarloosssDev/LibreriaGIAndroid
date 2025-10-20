package com.cibertec.controller

import android.content.Context
import com.cibertec.controller.api.RetrofitClient
import com.cibertec.model.CategoriaResponse
import com.cibertec.model.Category
import com.cibertec.model.CategoryWithProductCount
import com.cibertec.model.db.AppDatabase
import com.cibertec.model.repository.CategoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryController (context: Context) {
    private val repository: CategoryRepository
    private val apiService = RetrofitClient.instance

    init {
        val db = AppDatabase.getDatabase(context)
        repository = CategoryRepository(db.categoryDao())
    }

    fun loadCategories(
        onStartLoading: () -> Unit,
        onFinishLoading: (List<CategoryWithProductCount>) -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) { onStartLoading() }
                delay(1000)
                val categories = repository.getAllWithProductCount()
                withContext(Dispatchers.Main) { onFinishLoading(categories) }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) { onError(e) }
            }
        }
    }

    suspend fun getAll(): List<Category> {
        return repository.getAll()
    }
    suspend fun getCategoriasAPI(): List<CategoriaResponse> {
        return withContext(Dispatchers.IO) {
            val response = apiService.getCategorias()

            if (!response.isSuccessful) {
                throw Exception("Error de API al obtener categorías: ${response.code()}")
            }
            response.body() ?: emptyList()
        }
    }
}