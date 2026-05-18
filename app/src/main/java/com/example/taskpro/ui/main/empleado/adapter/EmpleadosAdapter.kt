package com.example.taskpro.ui.main.empleado.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskpro.R
import com.example.taskpro.ui.main.empleado.model.EmpleadoItem

class EmpleadosAdapter(
    private val lista: List<EmpleadoItem>
) : RecyclerView.Adapter<EmpleadosAdapter.ViewHolder>() {

    inner class ViewHolder(view: View)
        : RecyclerView.ViewHolder(view) {

        val tvNombre =
            view.findViewById<TextView>(
                R.id.tv_nombre_empleado
            )

        val tvCorreo =
            view.findViewById<TextView>(
                R.id.tv_correo_empleado
            )

        val btnAsignar =
            view.findViewById<Button>(
                R.id.btn_asignar_tarea
            )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_empleado,
                    parent,
                    false
                )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val empleado = lista[position]

        holder.tvNombre.text =
            "${empleado.nombres} ${empleado.apellidos}"

        holder.tvCorreo.text =
            empleado.correo

        holder.btnAsignar.setOnClickListener {

            // luego abriremos crear tarea
        }
    }

    override fun getItemCount(): Int {

        return lista.size
    }
}