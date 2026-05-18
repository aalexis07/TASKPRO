package com.example.taskpro.data

import com.example.taskpro.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.Serializable

object PermisosRepository {

    @Serializable
    data class PermisosData(

        val user_id: String,

        val crear_tareas: Boolean = false,
        val editar_tareas: Boolean = false,
        val ver_tareas: Boolean = false,
        val acceso_chat: Boolean = false,
        val gestionar_usuarios: Boolean = false,
        val ver_reportes: Boolean = false
    )

    suspend fun guardarPermisos(
        userId: String,
        crearTareas: Boolean,
        editarTareas: Boolean,
        verTareas: Boolean,
        accesoChat: Boolean,
        gestionarUsuarios: Boolean,
        verReportes: Boolean
    ) {

        SupabaseClient.client
            .postgrest["permisos"]
            .insert(

                PermisosData(
                    user_id = userId,

                    crear_tareas = crearTareas,
                    editar_tareas = editarTareas,
                    ver_tareas = verTareas,
                    acceso_chat = accesoChat,
                    gestionar_usuarios = gestionarUsuarios,
                    ver_reportes = verReportes
                )
            )
    }
}