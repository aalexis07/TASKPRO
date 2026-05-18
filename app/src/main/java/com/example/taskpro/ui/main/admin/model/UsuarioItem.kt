package com.example.taskpro.ui.main.admin.model
import kotlinx.serialization.Serializable


@Serializable
data class UsuarioItem(

    val id: String = "",

    val nombres: String = "",

    val apellidos: String = "",

    val correo: String = "",

    val rol: String = "empleado"
)