package com.example.taskpro.ui.main.tareas.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.taskpro.R
import com.example.taskpro.SupabaseClient
import com.example.taskpro.ui.main.tareas.model.TareaItem
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import android.app.Activity
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog

import com.example.taskpro.ui.main.MainActivity

class TareasAdapter(
    private val lista: List<TareaItem>
) : RecyclerView.Adapter<TareasAdapter.ViewHolder>() {

    inner class ViewHolder(view: View)
        : RecyclerView.ViewHolder(view) {

        val tvTitulo =
            view.findViewById<TextView>(
                R.id.tv_titulo_tarea
            )

        val tvDescripcion =
            view.findViewById<TextView>(
                R.id.tv_descripcion_tarea
            )

        val tvDireccion =
            view.findViewById<TextView>(
                R.id.tv_direccion_tarea
            )

        val btnCompartirUbicacion =
            view.findViewById<Button>(
                R.id.btn_compartir_ubicacion
            )
        val btnSubirFoto =
            view.findViewById<Button>(
                R.id.btn_subir_foto
            )

        val tvPrioridad =
            view.findViewById<TextView>(
                R.id.tv_prioridad_tarea
            )

        val spinnerEstado =
            view.findViewById<Spinner>(
                R.id.spinner_estado
            )

        val btnActualizar =
            view.findViewById<Button>(
                R.id.btn_actualizar_estado
            )

        val btnMapa =
            view.findViewById<Button>(
                R.id.btn_abrir_mapa
            )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_tarea,
                    parent,
                    false
                )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val tarea = lista[position]

        holder.tvTitulo.text =
            tarea.titulo

        holder.tvDescripcion.text =
            tarea.descripcion

        holder.tvDireccion.text =
            tarea.direccion

        holder.tvPrioridad.text =
            tarea.prioridad.uppercase()

        val estados = listOf(
            "pendiente",
            "en proceso",
            "completada"
        )

        holder.spinnerEstado.adapter =
            ArrayAdapter(
                holder.itemView.context,
                android.R.layout.simple_spinner_dropdown_item,
                estados
            )

        holder.spinnerEstado.setSelection(
            estados.indexOf(tarea.estado)
        )

        holder.btnMapa.setOnClickListener {

            val uri = Uri.parse(

                "geo:${tarea.latitud},${tarea.longitud}" +

                        "?q=${tarea.latitud},${tarea.longitud}"
            )

            val intent = Intent(
                Intent.ACTION_VIEW,
                uri
            )

            intent.setPackage(
                "com.google.android.apps.maps"
            )

            holder.itemView.context
                .startActivity(intent)
        }

        holder.btnCompartirUbicacion.setOnClickListener {

            val fusedLocationClient =
                LocationServices
                    .getFusedLocationProviderClient(
                        holder.itemView.context
                    )

            if (
                ActivityCompat.checkSelfPermission(
                    holder.itemView.context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                Toast.makeText(
                    holder.itemView.context,
                    "Permiso ubicación denegado",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            fusedLocationClient
                .lastLocation
                .addOnSuccessListener { location ->

                    if (location != null) {

                        CoroutineScope(
                            Dispatchers.IO
                        ).launch {

                            try {

                                SupabaseClient.client
                                    .postgrest["tareas"]

                                    .update(
                                        {
                                            set(
                                                "latitud",
                                                location.latitude
                                            )

                                            set(
                                                "longitud",
                                                location.longitude
                                            )
                                        }
                                    ) {

                                        filter {

                                            eq(
                                                "id",
                                                tarea.id
                                            )
                                        }
                                    }

                                CoroutineScope(
                                    Dispatchers.Main
                                ).launch {

                                    Toast.makeText(
                                        holder.itemView.context,
                                        "Ubicación compartida",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            } catch (e: Exception) {

                                CoroutineScope(
                                    Dispatchers.Main
                                ).launch {

                                    Toast.makeText(
                                        holder.itemView.context,
                                        "Error compartiendo ubicación",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    }
                }
        }

        holder.btnSubirFoto.setOnClickListener {

            val opciones = arrayOf(
                "Tomar foto",
                "Elegir de galería"
            )

            AlertDialog.Builder(
                holder.itemView.context
            )

                .setTitle(
                    "Seleccionar opción"
                )

                .setItems(opciones) { _, which ->

                    val activity =
                        holder.itemView.context
                                as MainActivity

                    when (which) {

                        0 -> {

                            Toast.makeText(
                                holder.itemView.context,
                                "Cámara próxima ",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        1 -> {

                            activity
                                .seleccionarImagen
                                .launch("image/*")
                        }
                    }
                }

                .show()
        }

        holder.btnActualizar.setOnClickListener {

            val nuevoEstado =
                holder.spinnerEstado
                    .selectedItem.toString()

            CoroutineScope(
                Dispatchers.IO
            ).launch {

                try {

                    SupabaseClient.client
                        .postgrest["tareas"]
                        .update(
                            {
                                set(
                                    "estado",
                                    nuevoEstado
                                )
                            }
                        ) {

                            filter {

                                eq(
                                    "id",
                                    tarea.id
                                )
                            }
                        }

                    CoroutineScope(
                        Dispatchers.Main
                    ).launch {

                        Toast.makeText(
                            holder.itemView.context,
                            "Estado actualizado",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } catch (e: Exception) {

                    CoroutineScope(
                        Dispatchers.Main
                    ).launch {

                        Toast.makeText(
                            holder.itemView.context,
                            "Error actualizando",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {

        return lista.size
    }
}