package com.example.taskpro.ui.main.admin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskpro.R
import com.example.taskpro.ui.main.admin.model.UsuarioItem
import kotlinx.serialization.Serializable

@Serializable
class UsuariosAdapter(


    private val listaUsuarios: List<UsuarioItem>

) : RecyclerView.Adapter<UsuariosAdapter.UsuarioViewHolder>() {

    inner class UsuarioViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val txtNombre: TextView =
            itemView.findViewById(R.id.txt_nombre)

        val txtCorreo: TextView =
            itemView.findViewById(R.id.txt_correo)

        val txtRol: TextView =
            itemView.findViewById(R.id.txt_rol)

        val btnEditar: Button =
            itemView.findViewById(R.id.btn_editar)

        val btnEliminar: Button =
            itemView.findViewById(R.id.btn_eliminar)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UsuarioViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_usuario,
                parent,
                false
            )

        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: UsuarioViewHolder,
        position: Int
    ) {

        val usuario = listaUsuarios[position]

        holder.txtNombre.text =
            "${usuario.nombres} ${usuario.apellidos}"

        holder.txtCorreo.text =
            usuario.correo

        holder.txtRol.text =
            usuario.rol.uppercase()

        holder.btnEditar.setOnClickListener {

        }

        holder.btnEliminar.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {

        return listaUsuarios.size
    }
}