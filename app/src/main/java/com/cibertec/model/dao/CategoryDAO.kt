package com.cibertec.model.dao

import androidx.room.*
import com.cibertec.model.Category

@Dao
interface CategoryDAO {
    @Query("DELETE FROM categories")
    fun clearCategories()
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