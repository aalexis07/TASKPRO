package com.example.taskpro.ui.main.empleado.model

import kotlinx.serialization.Serializable

@Serializable
data class EmpleadoItem(

    val id: String,

    val nombres: String,

    val apellidos: String,

    val correo: String,

    val rol: String
)