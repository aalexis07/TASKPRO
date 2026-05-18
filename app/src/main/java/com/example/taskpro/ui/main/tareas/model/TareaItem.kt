package com.example.taskpro.ui.main.tareas.model

import kotlinx.serialization.Serializable

@Serializable
data class TareaItem(

    val id: String,

    val titulo: String,

    val descripcion: String,

    val prioridad: String,

    val estado: String,

    val empleado_id: String,

    val direccion: String? = null,

    val latitud: Double? = null,

    val longitud: Double? = null
)