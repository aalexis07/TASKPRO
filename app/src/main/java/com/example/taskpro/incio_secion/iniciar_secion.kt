package com.example.taskpro.incio_secion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskpro.R

class iniciar_secion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_iniciar_secion)
        val btnRegister: Button = findViewById(R.id.recuperar_con)
        btnRegister.setOnClickListener {
            val intent = Intent(this, recuperar_con::class.java)
            startActivity(intent)
        }
    }
}