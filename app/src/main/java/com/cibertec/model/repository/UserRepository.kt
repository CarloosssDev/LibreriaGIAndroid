package com.cibertec.model.repository

import com.cibertec.model.User
import com.cibertec.model.dao.UserDAO

class UserRepository (private val userDao: UserDAO) {

    fun login(username: String, password: String): User? {
        return userDao.findByUsernameAndPassword(username, password)
    }


    fun saveUser(
        user: User,
        onUserSaved: () -> Unit,
        onUserAlreadyExists: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            val existingUser = userDao.findByUsername(user.username)
            if (existingUser != null) {
                onUserAlreadyExists()
            } else {
                userDao.save(user)
                onUserSaved()
            }
        } catch (e: Exception) {
            onError(e)
        }
    }

    fun getAllUsers(): List<User> {
        return userDao.findAll()
    }

    fun getByUsername(username: String): User? {
        return userDao.findByUsername(username)
    }
}