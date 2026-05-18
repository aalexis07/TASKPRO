package com.example.taskpro.ui.main.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.taskpro.R
import com.example.taskpro.SupabaseClient
import com.example.taskpro.data.UsuarioRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class PermisosFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_permisos,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(
            view,
            savedInstanceState
        )

        val switchCrearTareas =
            view.findViewById<Switch>(R.id.switch_crear_tareas)

        val switchEditarTareas =
            view.findViewById<Switch>(R.id.switch_editar_tareas)

        val switchVerTareas =
            view.findViewById<Switch>(R.id.switch_ver_tareas)

        val switchChat =
            view.findViewById<Switch>(R.id.switch_chat)

        val switchUsuarios =
            view.findViewById<Switch>(R.id.switch_usuarios)

        val switchReportes =
            view.findViewById<Switch>(R.id.switch_reportes)

        val btnGuardar =
            view.findViewById<Button>(R.id.btn_guardar_permisos)

        btnGuardar.setOnClickListener {

            val nombres =
                arguments?.getString("nombres") ?: ""

            val apellidos =
                arguments?.getString("apellidos") ?: ""

            val correo =
                arguments?.getString("correo") ?: ""

            val contrasena =
                arguments?.getString("contrasena") ?: ""

            val rol =
                arguments?.getString("rol") ?: "empleado"

            lifecycleScope.launch {

                try {

                    SupabaseClient.client.auth.signUpWith(Email) {

                        email = correo
                        password = contrasena

                        data = buildJsonObject {

                            put("nombres", nombres)
                            put("apellidos", apellidos)
                        }
                    }



                    val userId =
                        SupabaseClient.client.auth
                            .currentSessionOrNull()
                            ?.user
                            ?.id ?: ""

                    Log.d(
                        "DEBUG_USER",
                        "USER ID: $userId"
                    )

                    UsuarioRepository.insertarUsuario(
                        userId,
                        nombres,
                        apellidos,
                        correo,
                        rol
                    )

                    Log.d(
                        "DEBUG_USER",
                        "INSERT USUARIO OK"
                    )

                    SupabaseClient.client
                        .postgrest["permisos"]
                        .insert(

                            buildJsonObject {

                                put(
                                    "user_id",
                                    userId
                                )

                                put(
                                    "crear_tareas",
                                    switchCrearTareas.isChecked
                                )

                                put(
                                    "editar_tareas",
                                    switchEditarTareas.isChecked
                                )

                                put(
                                    "ver_tareas",
                                    switchVerTareas.isChecked
                                )

                                put(
                                    "acceso_chat",
                                    switchChat.isChecked
                                )

                                put(
                                    "gestionar_usuarios",
                                    switchUsuarios.isChecked
                                )

                                put(
                                    "ver_reportes",
                                    switchReportes.isChecked
                                )
                            }
                        )

                    Log.d(
                        "DEBUG_USER",
                        "INSERT PERMISOS OK"
                    )

                    requireActivity().runOnUiThread {

                        Toast.makeText(
                            requireContext(),
                            "Usuario creado correctamente",
                            Toast.LENGTH_LONG
                        ).show()

                        parentFragmentManager.popBackStack(
                            null,
                            androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                    }

                } catch (e: Exception) {

                    Log.e(
                        "DEBUG_USER",
                        "ERROR: ${e.message}"
                    )

                    requireActivity().runOnUiThread {

                        Toast.makeText(
                            requireContext(),
                            "Error: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
}