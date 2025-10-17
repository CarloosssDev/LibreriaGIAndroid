package com.cibertec.model.repository

import com.cibertec.model.Category
import com.cibertec.model.dao.CategoryDAO

class CategoryRepository (private val categoryDao: CategoryDAO) {
    suspend fun clearCategories() {
        categoryDao.clearCategories()
    }
    fun getAll(): List<Category> {
        return categoryDao.getAll()
    }
    fun getById(id: Int): Category {
        return categoryDao.getById(id)
    }
    fun insert(category: Category) {
        categoryDao.insert(category)
    }
    fun insertAll(categories: List<Category>) {
        categoryDao.insertAll(categories)
    }
    fun update(category: Category) {
        categoryDao.update(category)
    }
    fun delete(category: Category) {
        categoryDao.delete(category)
    }
}