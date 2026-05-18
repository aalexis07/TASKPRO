package com.example.taskpro.ui.main.tareas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.taskpro.R
import com.example.taskpro.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import kotlinx.serialization.Serializable

class CrearTareaFragment : Fragment() {

    @Serializable
    data class TareaData(

        val titulo: String,

        val descripcion: String,

        val prioridad: String,

        val empleado_id: String,

        val jefe_id: String,

        val direccion: String,

        val latitud: Double,

        val longitud: Double
    )

    private var empleadoId: String = ""
    private var latitud: Double = 0.0

    private var longitud: Double = 0.0

    companion object {

        fun newInstance(
            empleadoId: String
        ): CrearTareaFragment {

            val fragment =
                CrearTareaFragment()

            val bundle = Bundle()

            bundle.putString(
                "empleado_id",
                empleadoId
            )

            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        empleadoId =
            arguments?.getString(
                "empleado_id"
            ) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_crear_tarea,
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

        val etTitulo =
            view.findViewById<EditText>(
                R.id.et_titulo
            )

        val etDescripcion =
            view.findViewById<EditText>(
                R.id.et_descripcion
            )

        val etDireccion =
            view.findViewById<EditText>(
                R.id.et_direccion
            )

        val spinnerPrioridad =
            view.findViewById<Spinner>(
                R.id.spinner_prioridad
            )

        val btnGuardar =
            view.findViewById<Button>(
                R.id.btn_guardar_tarea
            )
        val btnUbicacion =
            view.findViewById<Button>(
                R.id.btn_obtener_ubicacion
            )

        val prioridades = listOf(
            "baja",
            "media",
            "alta"
        )

        spinnerPrioridad.adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                prioridades
            )
        btnUbicacion.setOnClickListener {

            val fusedLocationClient =
                LocationServices
                    .getFusedLocationProviderClient(
                        requireActivity()
                    )

            if (
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    1001
                )

                return@setOnClickListener
            }

            fusedLocationClient
                .lastLocation
                .addOnSuccessListener { location ->

                    if (location != null) {

                        latitud = location.latitude

                        longitud = location.longitude

                        etDireccion.setText(
                            "Lat: $latitud\nLng: $longitud"
                        )

                        Toast.makeText(
                            requireContext(),
                            "Ubicación obtenida",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
        btnGuardar.setOnClickListener {

            val titulo =
                etTitulo.text.toString().trim()

            val descripcion =
                etDescripcion.text.toString().trim()

            val direccion =
                etDireccion.text.toString().trim()

            val prioridad =
                spinnerPrioridad.selectedItem.toString()

            if (
                titulo.isEmpty() ||
                descripcion.isEmpty() ||
                direccion.isEmpty()
            ) {

                Toast.makeText(
                    requireContext(),
                    "Completa todos los campos",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            lifecycleScope.launch {

                try {

                    val jefeId =
                        SupabaseClient.client.auth
                            .currentUserOrNull()
                            ?.id ?: ""

                    SupabaseClient.client
                        .postgrest["tareas"]
                        .insert(

                            TareaData(
                                titulo = titulo,
                                descripcion = descripcion,
                                prioridad = prioridad,
                                empleado_id = empleadoId,
                                jefe_id = jefeId,
                                direccion = direccion,
                                latitud = latitud,
                                longitud = longitud
                            )
                            )



                    requireActivity().runOnUiThread {

                        Toast.makeText(
                            requireContext(),
                            "Tarea creada",
                            Toast.LENGTH_SHORT
                        ).show()

                        parentFragmentManager
                            .popBackStack()
                    }

                } catch (e: Exception) {

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