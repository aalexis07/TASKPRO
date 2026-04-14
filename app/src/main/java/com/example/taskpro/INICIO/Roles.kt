package com.example.taskpro.INICIO

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskpro.R

class Roles : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_roles)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, SELECT_ROLESD:: class.java ))
            finish()
        }, 3000)
    }
}