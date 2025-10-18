package com.cibertec.model.db

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cibertec.model.*
import com.cibertec.model.dao.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class, Category::class, Product::class, Ingreso::class, Salida::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO
    abstract fun categoryDao(): CategoryDAO
    abstract fun productDao(): ProductDAO
    abstract fun ingresoDao(): IngresoDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val DB_NAME = "libreria.db"

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                val dbInstance = getDatabase(context)
                                dbInstance.userDao().insertAll(User.getUsers())
                                dbInstance.categoryDao().insertAll(Category.getCategories())
                                dbInstance.productDao().insertAll(Product.getProducts())
                                dbInstance.ingresoDao().insertAll(Ingreso.getIngresos())
                            }
                        }
                    })
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}