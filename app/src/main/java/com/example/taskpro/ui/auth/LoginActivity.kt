package com.example.taskpro.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.taskpro.R
import com.example.taskpro.data.CredencialesManager
import com.example.taskpro.ui.main.MainActivity
import com.example.taskpro.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var etCorreo: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var txtRecuperar: TextView
    private lateinit var txtVolver: TextView
    private lateinit var txtHuella: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        // Referencias
        etCorreo = findViewById(R.id.et_correo)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        txtRecuperar = findViewById(R.id.txt_recuperar)
        txtVolver = findViewById(R.id.txt_volver)
        txtHuella = findViewById(R.id.txt_huella)

        // Botón volver
        txtVolver.setOnClickListener {

            finish()

        }

        // Recuperar contraseña
        txtRecuperar.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    RecuperarContrasenaActivity::class.java
                )
            )

        }

        // Login
        btnLogin.setOnClickListener {

            iniciarSesion()

        }
        txtHuella.setOnClickListener {

            mostrarDialogoHuella()

        }

        configurarVisibilidadHuella()
    }

    override fun onResume() {
        super.onResume()

        configurarVisibilidadHuella()
    }

    private fun iniciarSesion() {

        val correo = etCorreo.text.toString().trim()
        val contrasena = etPassword.text.toString().trim()

        if (correo.isEmpty() || contrasena.isEmpty()) {

            Toast.makeText(
                this,
                "Completa todos los campos",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (contrasena.length < 6) {

            Toast.makeText(
                this,
                "La contraseña debe tener mínimo 6 caracteres",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        lifecycleScope.launch {

            try {

                SupabaseClient.client.auth.signInWith(Email) {



                    email = correo
                    password = contrasena

                }

                CredencialesManager.guardarCredenciales(
                    this@LoginActivity,
                    correo,
                    contrasena
                )

                irAPantallaPrincipal()

            } catch (e: Exception) {

                val mensaje = when {

                    e.message?.contains("Invalid login credentials") == true ->
                        "Correo o contraseña incorrectos"

                    else ->
                        "Error al iniciar sesión"

                }

                Toast.makeText(
                    this@LoginActivity,
                    mensaje,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun configurarVisibilidadHuella() {

        val biometricManager = BiometricManager.from(this)

        val biometriaDisponible =
            biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG
            ) == BiometricManager.BIOMETRIC_SUCCESS



        txtHuella.visibility =
            if (biometriaDisponible)
                View.VISIBLE
            else
                View.GONE

    }

    private fun mostrarDialogoHuella() {

        val executor = ContextCompat.getMainExecutor(this)

        val biometricPrompt = BiometricPrompt(
            this,
            executor,

            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {

                    val correo =
                        CredencialesManager.obtenerCorreo(this@LoginActivity)

                    val contrasena =
                        CredencialesManager.obtenerContrasena(this@LoginActivity)

                    if (correo != null && contrasena != null) {

                        lifecycleScope.launch {

                            try {

                                SupabaseClient.client.auth.signInWith(Email) {

                                    email = correo
                                    password = contrasena

                                }

                                irAPantallaPrincipal()

                            } catch (e: Exception) {

                                Toast.makeText(
                                    this@LoginActivity,
                                    "Error al iniciar sesión",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }

                override fun onAuthenticationFailed() {

                    Toast.makeText(
                        this@LoginActivity,
                        "Huella no reconocida",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Acceso con huella")
            .setSubtitle("Usa tu huella para ingresar")
            .setNegativeButtonText("Cancelar")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun irAPantallaPrincipal() {

        startActivity(
            Intent(
                this,
                MainActivity::class.java
            )
        )

        finishAffinity()
    }
}