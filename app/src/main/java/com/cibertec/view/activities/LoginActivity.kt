package com.cibertec.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.cibertec.R
import com.cibertec.controller.AuthController
import com.cibertec.view.dialogs.RegisterUserDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private val controller by lazy { AuthController(this) }
    private lateinit var googleSignInClient: GoogleSignInClient
    private val auth = FirebaseAuth.getInstance()
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val txtUsername = findViewById<EditText>(R.id.txtUsername)
        val txtPassword = findViewById<EditText>(R.id.txtPassword)
        val btnLogin = findViewById<MaterialButton>(R.id.btnLogin)
        val btnRegister = findViewById<MaterialButton>(R.id.btnRegister)
        val btnGoogleSignIn = findViewById<MaterialButton>(R.id.btnGoogleSignIn)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

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
        btnGoogleSignIn.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account?.idToken)
            } catch (e: ApiException) {
                Log.w("LoginActivity", "Error al iniciar sesión con Google", e)
                Toast.makeText(this, "Error al iniciar sesión con Google", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Bienvenido ${user?.displayName}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al iniciar sesión con Google", Toast.LENGTH_SHORT).show()
                }
            }
    }
}