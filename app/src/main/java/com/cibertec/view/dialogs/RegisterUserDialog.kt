package com.cibertec.view.dialogs

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.Toast
import com.cibertec.R
import com.cibertec.controller.AuthController
import com.cibertec.model.User
import com.cibertec.model.db.AppDatabase
import com.cibertec.model.repository.UserRepository
import com.cibertec.view.activities.LoginActivity
import com.google.android.material.button.MaterialButton

class RegisterUserDialog (
    private val context: Context,
    private val controller: AuthController,
    private val onError: (Throwable) -> Unit
)  {
    private val dialog: Dialog = Dialog(context)

    init {

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_register_user)
        dialog.setCancelable(true)

        val etName = dialog.findViewById<EditText>(R.id.etName)
        val etPhone = dialog.findViewById<EditText>(R.id.etPhone)
        val etUsername = dialog.findViewById<EditText>(R.id.etUsername)
        val etPassword = dialog.findViewById<EditText>(R.id.etPassword)
        val btnRegister = dialog.findViewById<MaterialButton>(R.id.btnRegister)
        val btnCancel = dialog.findViewById<MaterialButton>(R.id.btnCancel)

        btnRegister.setOnClickListener {
            val name = etName.text.toString()
            val phone = etPhone.text.toString()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (name.isEmpty() || phone.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val user = User(name, phone, username, password)

            controller.register(
                user = user,
                onSuccess = {
                    (context as? LoginActivity)?.runOnUiThread {
                        Toast.makeText(context, "Usuario registrado exitosamente", Toast.LENGTH_SHORT
                        ).show()
                        dialog.dismiss()
                    }},
                onUserAlreadyExists = {
                    (context as? LoginActivity)?.runOnUiThread {
                        Toast.makeText(context, "El nombre de usuario ya está en uso", Toast.LENGTH_SHORT).show()
                        return@runOnUiThread
                    }},
                onError = {
                    Log.e("LoginActivity", "Error al registrar usuario", it)
                }
            )

        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

    }
    fun show() {
        try {
            dialog.show()
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        } catch (e: Exception) {
            onError(e)
        }
    }
}