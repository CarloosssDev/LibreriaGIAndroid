package com.cibertec.controller

import android.content.Context
import com.cibertec.model.User
import com.cibertec.model.db.AppDatabase
import com.cibertec.model.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthController (context: Context) {
    private var repository: UserRepository

    init {
        val db = AppDatabase.getDatabase(context)
        repository = UserRepository(db.userDao())
    }

    fun login(
        username: String,
        password: String,
        onSuccess: (User) -> Unit,
        onUserNotFound: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user = repository.login(username, password)
                if(user != null) {
                    onSuccess(user)
                } else {
                    onUserNotFound()
                }
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
    fun register(
        user: User,
        onSuccess: () -> Unit = {},
        onUserAlreadyExists: () -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.saveUser(
                    user = user,
                    onUserSaved = {
                        onSuccess()
                    },
                    onUserAlreadyExists = {
                        onUserAlreadyExists()
                    },
                    onError = {
                        onError(it)
                    }

                )
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}