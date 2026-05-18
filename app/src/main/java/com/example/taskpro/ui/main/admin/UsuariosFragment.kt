package com.example.taskpro.ui.main.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskpro.R
import com.example.taskpro.SupabaseClient
import com.example.taskpro.ui.main.admin.adapter.UsuariosAdapter
import com.example.taskpro.ui.main.admin.model.UsuarioItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class UsuariosFragment : Fragment() {

    private lateinit var recyclerUsuarios: RecyclerView

    private lateinit var fabCrearUsuario:
            FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_usuarios,
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

        recyclerUsuarios =
            view.findViewById(R.id.recycler_usuarios)

        fabCrearUsuario =
            view.findViewById(R.id.fab_crear_usuario)

        recyclerUsuarios.layoutManager =
            LinearLayoutManager(requireContext())

        cargarUsuarios()

        fabCrearUsuario.setOnClickListener {

            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    CrearUsuarioFragment()
                )
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        cargarUsuarios()
    }

    private fun cargarUsuarios() {

        lifecycleScope.launch {

            try {

                val response =
                    SupabaseClient.client
                        .postgrest["usuarios"]
                        .select()

                val listaUsuarios =
                    response.decodeAs<List<UsuarioItem>>()

                recyclerUsuarios.adapter =
                    UsuariosAdapter(listaUsuarios)

            } catch (e: Exception) {

                e.printStackTrace()

                Toast.makeText(
                    requireContext(),
                    "Error cargando usuarios: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}