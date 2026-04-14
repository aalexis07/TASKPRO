package com.example.taskpro.INICIO

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskpro.R

class SELECT_ROLESD : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_select_rolesd)
        val btn: Button = findViewById(R.id.button_ingresar)
        btn.setOnClickListener{
            val intent : Intent = Intent (this, roles_barios:: class.java)
            startActivity(intent)
        }
    }
}