package com.cibertec.controller

import android.content.Context
import com.cibertec.model.Category
import com.cibertec.model.db.AppDatabase
import com.cibertec.model.repository.CategoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryController (context: Context) {
    private val repository: CategoryRepository

    init {
        val db = AppDatabase.getDatabase(context)
        repository = CategoryRepository(db.categoryDao())
    }

    fun loadCategories(
        onStartLoading: () -> Unit,
        onFinishLoading: (List<Category>) -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) { onStartLoading() }
                delay(2000)
                val categories = repository.getAll()
                withContext(Dispatchers.Main) { onFinishLoading(categories) }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) { onError(e) }
            }
        }
    }

    suspend fun getAll(): List<Category> {
        return repository.getAll()
    }
}