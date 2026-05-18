package com.example.taskpro.ui.inicio

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.taskpro.R
import com.example.taskpro.ui.auth.LoginActivity

class BienvenidoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_bienvenido)

        val btnIngresar = findViewById<Button>(R.id.btn_inicio_ingresar)


        btnIngresar.setOnClickListener {

            startActivity(Intent(this, LoginActivity::class.java))

        }


        }

}