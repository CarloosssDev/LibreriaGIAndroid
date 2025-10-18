package com.cibertec.controller

import android.content.Context
import com.cibertec.model.Category
import com.cibertec.model.Product
import com.cibertec.model.db.AppDatabase
import com.cibertec.model.repository.CategoryRepository
import com.cibertec.model.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductController(context: Context) {
    private val productRepository: ProductRepository
    private val categoryRepository: CategoryRepository

    init {
        val db = AppDatabase.getDatabase(context)
        productRepository = ProductRepository(db.productDao())
        categoryRepository = CategoryRepository(db.categoryDao())
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

    fun insertProduct(
        product: Product,
        onInserted: () -> Unit = {},
        onError: (Exception) -> Unit = {}
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                productRepository.insert(product)
                withContext(Dispatchers.Main) { onInserted() }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { onError(e) }
            }
        }
    }

    fun updateProduct(
        product: Product,
        onUpdated: () -> Unit = {},
        onError: (Exception) -> Unit = {}
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                productRepository.update(product)
                withContext(Dispatchers.Main) { onUpdated() }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { onError(e) }
            }
        }
    }

    fun deleteProduct(
        product: Product,
        onDeleted: () -> Unit = {},
        onError: (Exception) -> Unit = {}
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                productRepository.delete(product)
                withContext(Dispatchers.Main) { onDeleted() }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { onError(e) }
            }
        }
    }
}