package com.example.taskpro.ui.main.empleado

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
import com.example.taskpro.ui.main.empleado.adapter.EmpleadosAdapter
import com.example.taskpro.ui.main.empleado.model.EmpleadoItem
import io.github.jan.supabase.postgrest.postgrest

import kotlinx.coroutines.launch

class EmpleadoFragment : Fragment() {

    private lateinit var recyclerEmpleados:
            RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_empleado,
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

        recyclerEmpleados =
            view.findViewById(R.id.recycler_empleados)

        recyclerEmpleados.layoutManager =
            LinearLayoutManager(requireContext())

        cargarEmpleados()
    }

    private fun cargarEmpleados() {

        lifecycleScope.launch {

            try {

                val empleados =
                    SupabaseClient.client
                        .postgrest["usuarios"]
                        .select()

                        .decodeList<EmpleadoItem>()

                        .filter {
                            it.rol == "empleado"
                        }

                recyclerEmpleados.adapter =
                    EmpleadosAdapter(empleados)

            } catch (e: Exception) {

                e.printStackTrace()

                Toast.makeText(
                    requireContext(),
                    "Error cargando empleados",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}