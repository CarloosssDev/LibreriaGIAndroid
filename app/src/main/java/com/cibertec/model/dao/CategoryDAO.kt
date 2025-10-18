package com.cibertec.model.dao

import androidx.room.*
import com.cibertec.model.Category
import com.cibertec.model.CategoryWithProductCount

@Dao
interface CategoryDAO {
    @Transaction
    @Query("""
        SELECT c.*, (SELECT COUNT(*) FROM products p WHERE p.categoryId = c.id) as product_count
        FROM categories c
        """)
    fun getAllWithProductCount(): List<CategoryWithProductCount>
    @Query("SELECT * FROM categories")
    fun getAll(): List<Category>
    @Query("SELECT * FROM categories WHERE id = :id")
    fun getById(id: Int): Category
    @Insert
    fun insert(category: Category)
    @Insert
    fun insertAll(categories: List<Category>)
    @Update
    fun update(category: Category)
    @Delete
    fun delete(category: Category)
}