package com.cibertec.model.dao

import androidx.room.*
import com.cibertec.model.User

@Dao
interface UserDAO {
    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    fun findByUsernameAndPassword(username: String, password: String): User?

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    fun findByUsername(username: String): User?

    @Query("SELECT * FROM users")
    fun findAll(): List<User>

    @Insert
    fun save(user: User)

    @Insert
    fun insertAll(users: List<User>)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)
}