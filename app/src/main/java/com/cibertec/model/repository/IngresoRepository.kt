package com.cibertec.model.repository

import com.cibertec.model.Ingreso
import com.cibertec.model.dao.IngresoDAO

class IngresoRepository (private val ingresoDao: IngresoDAO) {
    fun getAll(): List<Ingreso> {
        return ingresoDao.getAll()
    }
    fun insert(ingreso: Ingreso) {
        ingresoDao.insert(ingreso)
    }
    fun insertAll(ingresos: List<Ingreso>) {
        ingresoDao.insertAll(ingresos)
    }
    fun update(ingreso: Ingreso) {
        ingresoDao.update(ingreso)
    }
    fun delete(ingreso: Ingreso) {
        ingresoDao.delete(ingreso)
    }
}