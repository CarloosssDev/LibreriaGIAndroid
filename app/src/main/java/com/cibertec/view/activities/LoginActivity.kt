package com.cibertec.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cibertec.R
import com.cibertec.controller.AuthController
import com.cibertec.view.dialogs.RegisterUserDialog
import com.google.android.material.button.MaterialButton

class LoginActivity : AppCompatActivity() {

    private val controller by lazy { AuthController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val txtUsername = findViewById<EditText>(R.id.txtUsername)
        val txtPassword = findViewById<EditText>(R.id.txtPassword)
        val btnLogin = findViewById<MaterialButton>(R.id.btnLogin)
        val btnRegister = findViewById<MaterialButton>(R.id.btnRegister)

        btnLogin.setOnClickListener {
            val username = txtUsername.text.toString()
            val password = txtPassword.text.toString()

            if(username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Ingrese sus credenciales", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            controller.login(
                username = username,
                password = password,
                onSuccess = { user ->
                    runOnUiThread {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Bienvenido ${user.name}", Toast.LENGTH_SHORT).show()
                }},
                onUserNotFound = {
                    runOnUiThread {
                    Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                }},
                onError = { error ->
                    runOnUiThread {
                    Log.e("LoginActivity", "Error al iniciar sesión", error)
                }}
            )
        }
        btnRegister.setOnClickListener {
            RegisterUserDialog(
                context = this,
                controller = controller,
                onError = {
                    Log.e("LoginActivity", "Error al registrar usuario", it)
                }
            ).show()
        }

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}