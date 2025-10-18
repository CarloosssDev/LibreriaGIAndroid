package com.cibertec.model.dao

import androidx.room.*
import com.cibertec.model.Ingreso

@Dao
interface IngresoDAO {
    @Query("SELECT * FROM ingresos")
    fun getAll(): List<Ingreso>
    @Insert
    fun insert(ingreso: Ingreso)
    @Insert
    fun insertAll(ingresos: List<Ingreso>)
    @Update
    fun update(ingreso: Ingreso)
    @Delete
    fun delete(ingreso: Ingreso)
}