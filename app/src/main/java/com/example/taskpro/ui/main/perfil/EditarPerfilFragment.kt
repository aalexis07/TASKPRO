package com.example.taskpro.ui.main.perfil

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.example.taskpro.SupabaseClient
import com.example.taskpro.data.UsuarioRepository
import com.example.taskpro.R

import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch
import java.io.File

class EditarPerfilFragment : Fragment() {

    private var uriFotoSeleccionada: Uri? = null

    private lateinit var ivEditarFoto: ImageView
    private lateinit var archivoFotoTemp: File

    // =========================
    // PERMISO CAMARA
    // =========================

    private val lanzadorPermisoCamara =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { concedido ->

            if (concedido) {

                abrirCamara()

            } else {

                Toast.makeText(
                    requireContext(),
                    "Permiso de cámara denegado",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    // =========================
    // CAMARA
    // =========================

    private val lanzadorCamara =
        registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { exito ->

            if (exito) {

                uriFotoSeleccionada =
                    Uri.fromFile(archivoFotoTemp)

                ivEditarFoto.load(uriFotoSeleccionada) {

                    transformations(
                        CircleCropTransformation()
                    )

                    memoryCachePolicy(CachePolicy.DISABLED)
                    diskCachePolicy(CachePolicy.DISABLED)
                }
            }
        }

    // =========================
    // GALERIA
    // =========================

    private val lanzadorGaleria =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->

            if (uri != null) {

                uriFotoSeleccionada = uri

                ivEditarFoto.load(uri) {

                    transformations(
                        CircleCropTransformation()
                    )
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_editar_perfil,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(view, savedInstanceState)

        // =========================
        // VISTAS
        // =========================

        ivEditarFoto =
            view.findViewById(R.id.iv_editar_foto)

        val ivCamara =
            view.findViewById<ImageView>(
                R.id.iv_camara_icon
            )

        val etNombres =
            view.findViewById<EditText>(
                R.id.et_editar_nombres
            )

        val etApellidos =
            view.findViewById<EditText>(
                R.id.et_editar_apellidos
            )

        val etCorreo =
            view.findViewById<EditText>(
                R.id.et_editar_correo
            )

        val etContrasena =
            view.findViewById<EditText>(
                R.id.et_editar_contrasena
            )

        val etReContrasena =
            view.findViewById<EditText>(
                R.id.et_editar_recontrasena
            )

        val btnGuardar =
            view.findViewById<Button>(
                R.id.btn_guardar_perfil
            )

        // =========================
        // CARGAR USUARIO
        // =========================

        lifecycleScope.launch {

            val usuario =
                UsuarioRepository.obtenerUsuarioActual()

            if (usuario != null) {

                etNombres.setText(usuario.nombres)

                etApellidos.setText(usuario.apellidos)

                etCorreo.setText(
                    usuario.correo ?: ""
                )

                if (!usuario.foto_url.isNullOrEmpty()) {

                    val urlConTimestamp =
                        "${usuario.foto_url}?t=${System.currentTimeMillis()}"

                    ivEditarFoto.load(urlConTimestamp) {

                        transformations(
                            CircleCropTransformation()
                        )

                        placeholder(
                            R.mipmap.ic_launcher_round
                        )

                        error(
                            R.mipmap.ic_launcher_round
                        )

                        memoryCachePolicy(
                            CachePolicy.DISABLED
                        )

                        diskCachePolicy(
                            CachePolicy.DISABLED
                        )
                    }
                }
            }
        }

        // =========================
        // EVENTOS
        // =========================

        ivCamara.setOnClickListener {

            mostrarOpcionesFoto()
        }

        btnGuardar.setOnClickListener {

            guardarCambios(
                etNombres,
                etApellidos,
                etCorreo,
                etContrasena,
                etReContrasena
            )
        }
    }

    // =========================
    // DIALOGO FOTO
    // =========================

    private fun mostrarOpcionesFoto() {

        val opciones = arrayOf(
            "Tomar foto",
            "Elegir de galería"
        )

        android.app.AlertDialog.Builder(
            requireContext()
        )
            .setTitle("Foto de perfil")
            .setItems(opciones) { _, cual ->

                when (cual) {

                    0 -> verificarPermisoCamara()

                    1 -> lanzadorGaleria.launch(
                        "image/*"
                    )
                }
            }
            .show()
    }

    // =========================
    // VERIFICAR CAMARA
    // =========================

    private fun verificarPermisoCamara() {

        when {

            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {

                abrirCamara()
            }

            shouldShowRequestPermissionRationale(
                Manifest.permission.CAMERA
            ) -> {

                android.app.AlertDialog.Builder(
                    requireContext()
                )
                    .setTitle("Permiso requerido")
                    .setMessage(
                        "Necesitamos acceso a la cámara para tomar tu foto."
                    )
                    .setPositiveButton("Aceptar") { _, _ ->

                        lanzadorPermisoCamara.launch(
                            Manifest.permission.CAMERA
                        )
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }

            else -> {

                lanzadorPermisoCamara.launch(
                    Manifest.permission.CAMERA
                )
            }
        }
    }

    // =========================
    // ABRIR CAMARA
    // =========================

    private fun abrirCamara() {

        val carpeta = File(
            requireContext().cacheDir,
            "images"
        )

        carpeta.mkdirs()

        archivoFotoTemp = File(
            carpeta,
            "foto_perfil_temp.jpg"
        )

        val uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            archivoFotoTemp
        )

        lanzadorCamara.launch(uri)
    }

    // =========================
    // GUARDAR CAMBIOS
    // =========================

    private fun guardarCambios(
        etNombres: EditText,
        etApellidos: EditText,
        etCorreo: EditText,
        etContrasena: EditText,
        etReContrasena: EditText
    ) {

        val nombres =
            etNombres.text.toString().trim()

        val apellidos =
            etApellidos.text.toString().trim()

        val correo =
            etCorreo.text.toString().trim()

        val contrasena =
            etContrasena.text.toString()

        val reContrasena =
            etReContrasena.text.toString()

        // =========================
        // VALIDACIONES
        // =========================

        if (
            nombres.isEmpty() ||
            apellidos.isEmpty() ||
            correo.isEmpty()
        ) {

            Toast.makeText(
                requireContext(),
                "Completa todos los campos",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (contrasena.isNotEmpty()) {

            if (contrasena.length < 6) {

                Toast.makeText(
                    requireContext(),
                    "La contraseña debe tener mínimo 6 caracteres",
                    Toast.LENGTH_SHORT
                ).show()

                return
            }

            if (contrasena != reContrasena) {

                Toast.makeText(
                    requireContext(),
                    "Las contraseñas no coinciden",
                    Toast.LENGTH_SHORT
                ).show()

                return
            }
        }

        // =========================
        // ACTUALIZAR
        // =========================

        lifecycleScope.launch {

            try {

                var fotoUrl: String? = null

                if (uriFotoSeleccionada != null) {

                    fotoUrl =
                        UsuarioRepository.subirFotoPerfil(
                            requireContext(),
                            uriFotoSeleccionada!!
                        )
                }

                UsuarioRepository.actualizarPerfil(
                    nombres = nombres,
                    apellidos = apellidos,
                    correo = correo,
                    fotoUrl = fotoUrl
                )

                if (contrasena.isNotEmpty()) {

                    SupabaseClient.client.auth.updateUser {

                        password = contrasena
                    }
                }

                activity?.runOnUiThread {

                    Toast.makeText(
                        requireContext(),
                        "Perfil actualizado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()

                    parentFragmentManager.popBackStack()
                }

            } catch (e: Exception) {

                activity?.runOnUiThread {

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