package com.example.taskpro.INICIO

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskpro.mi_main.MainActivity
import com.example.taskpro.R
import com.example.taskpro.incio_secion.iniciar_secion

class roles_barios : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_roles_barios)
        val btnRegister: Button = findViewById(R.id.admin)
        btnRegister.setOnClickListener {
            val intent = Intent(this, iniciar_secion::class.java)
            startActivity(intent)
        }
    }
}