package com.example.taskpro.ui.main.tareas

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
import com.example.taskpro.ui.main.tareas.adapter.TareasAdapter
import com.example.taskpro.ui.main.tareas.model.TareaItem
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest

import kotlinx.coroutines.launch

class TareasFragment : Fragment() {

    private lateinit var recyclerTareas:
            RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_tareas,
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

        recyclerTareas =
            view.findViewById(R.id.recycler_tareas)

        recyclerTareas.layoutManager =
            LinearLayoutManager(requireContext())

        cargarTareas()
    }

    private fun cargarTareas() {

        lifecycleScope.launch {

            try {

                val userId =
                    SupabaseClient.client.auth
                        .currentUserOrNull()
                        ?.id ?: ""

                val tareas =
                    SupabaseClient.client
                        .postgrest["tareas"]
                        .select()

                        .decodeList<TareaItem>()

                        .filter {
                            it.empleado_id == userId
                        }

                recyclerTareas.adapter =
                    TareasAdapter(tareas)

            } catch (e: Exception) {

                e.printStackTrace()

                Toast.makeText(
                    requireContext(),
                    "Error cargando tareas",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}