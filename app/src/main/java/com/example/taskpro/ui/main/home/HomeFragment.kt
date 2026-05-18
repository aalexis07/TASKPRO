package com.example.taskpro.ui.main.home

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.taskpro.R
import com.example.taskpro.data.UsuarioRepository
import com.example.taskpro.ui.main.admin.UsuariosFragment
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var moduloUsuarios: LinearLayout
    private lateinit var moduloTareas: LinearLayout
    private lateinit var moduloChat: LinearLayout
    private lateinit var moduloPerfil: LinearLayout

    private lateinit var txtSaludo: TextView
    private lateinit var txtSubtitulo: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Referencias

        moduloUsuarios = view.findViewById(R.id.modulo_usuarios)
        moduloTareas = view.findViewById(R.id.modulo_tareas)
        moduloChat = view.findViewById(R.id.modulo_chat)
        moduloPerfil = view.findViewById(R.id.modulo_perfil)

        txtSaludo = view.findViewById(R.id.txt_saludo)
        txtSubtitulo = view.findViewById(R.id.txt_subtitulo)

        cargarInformacionUsuario()

        moduloUsuarios.setOnClickListener {

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, UsuariosFragment())
                .addToBackStack(null)
                .commit()
        }

    }

    private fun cargarInformacionUsuario() {

        lifecycleScope.launch {

            val usuario = UsuarioRepository.obtenerUsuarioActual()

            val rol = usuario?.rol ?: "empleado"

            activity?.runOnUiThread {

                txtSaludo.text =
                    "Hola, ${usuario?.nombres ?: "Usuario"}"

                when (rol) {

                    "admin" -> {

                        txtSubtitulo.text =
                            "Panel de administración del sistema"

                        moduloUsuarios.visibility = View.VISIBLE
                    }

                    "jefe" -> {

                        txtSubtitulo.text =
                            "Gestión de equipo y tareas"

                        moduloUsuarios.visibility = View.GONE
                    }

                    "empleado" -> {

                        txtSubtitulo.text =
                            "Consulta tus tareas y actividades"

                        moduloUsuarios.visibility = View.GONE
                    }

                    else -> {

                        moduloUsuarios.visibility = View.GONE
                    }
                }
            }
        }
    }
}