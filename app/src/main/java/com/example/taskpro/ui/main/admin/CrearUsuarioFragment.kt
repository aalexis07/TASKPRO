package com.example.taskpro.ui.main.admin

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
import com.example.taskpro.R

class CrearUsuarioFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_crear_usuario,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(view, savedInstanceState)

        val etNombres =
            view.findViewById<EditText>(R.id.et_nombres)

        val etApellidos =
            view.findViewById<EditText>(R.id.et_apellidos)

        val etCorreo =
            view.findViewById<EditText>(R.id.et_correo)

        val etContrasena =
            view.findViewById<EditText>(R.id.et_contrasena)

        val spinnerRol =
            view.findViewById<Spinner>(R.id.spinner_rol)

        val btnCrear =
            view.findViewById<Button>(R.id.btn_crear_usuario)

        val roles = listOf(
            "admin",
            "jefe",
            "empleado"
        )

        spinnerRol.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            roles
        )

        btnCrear.setOnClickListener {

            val nombres =
                etNombres.text.toString().trim()

            val apellidos =
                etApellidos.text.toString().trim()

            val correo =
                etCorreo.text.toString().trim()

            val contrasena =
                etContrasena.text.toString().trim()

            val rol =
                spinnerRol.selectedItem.toString()

            if (
                nombres.isEmpty() ||
                apellidos.isEmpty() ||
                correo.isEmpty() ||
                contrasena.isEmpty()
            ) {

                Toast.makeText(
                    requireContext(),
                    "Completa todos los campos",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val fragment = PermisosFragment()

            val bundle = Bundle()

            bundle.putString(
                "nombres",
                nombres
            )

            bundle.putString(
                "apellidos",
                apellidos
            )

            bundle.putString(
                "correo",
                correo
            )

            bundle.putString(
                "contrasena",
                contrasena
            )

            bundle.putString(
                "rol",
                rol
            )

            fragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    fragment
                )
                .addToBackStack(null)
                .commit()
        }
    }
}