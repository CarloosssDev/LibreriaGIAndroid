package com.cibertec.model.repository

import com.cibertec.model.Product
import com.cibertec.model.dao.ProductDAO

class ProductRepository (private val productDao: ProductDAO) {
    fun getAll(): List<Product> {
        return productDao.getAll()
    }
    fun insert(product: Product) {
        productDao.insert(product)
    }
    fun insertAll(products: List<Product>) {
        productDao.insertAll(products)
    }
    fun update(product: Product) {
        productDao.update(product)
    }
    fun delete(product: Product) {
        productDao.delete(product)
    }
}