package com.cibertec.model.dao

import androidx.room.*
import com.cibertec.model.Product

@Dao
interface ProductDAO {
    @Query("SELECT * FROM products")
    fun getAll(): List<Product>
    @Insert
    fun insert(product: Product)
    @Insert
    fun insertAll(products: List<Product>)
    @Update
    fun update(product: Product)
    @Delete
    fun delete(product: Product)
}