package com.example.taskpro.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.taskpro.R
import com.example.taskpro.SupabaseClient
import com.example.taskpro.data.CredencialesManager
import com.example.taskpro.data.UsuarioRepository
import com.example.taskpro.ui.auth.LoginActivity
import com.example.taskpro.ui.main.admin.AdminFragment
import com.example.taskpro.ui.main.chat.ChatFragment
import com.example.taskpro.ui.main.empleado.EmpleadoFragment
import com.example.taskpro.ui.main.jefe.JefeFragment
import com.example.taskpro.ui.main.perfil.PerfilFragment
import com.example.taskpro.ui.main.tareas.TareasFragment
import com.example.taskpro.ui.main.admin.UsuariosFragment
import com.example.taskpro.ui.main.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        val navView = findViewById<NavigationView>(R.id.nav_view)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.app_name,
            R.string.app_name
        )

        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        toggle.drawerArrowDrawable.color =
            ContextCompat.getColor(this, R.color.white)

        // Fragment inicial
        cargarFragment(HomeFragment())

        bottomNav.selectedItemId = R.id.nav_home

        // Configurar menú según rol
        configurarMenuPorRol(navView.menu)

        // Bottom Navigation
        bottomNav.setOnItemSelectedListener { item ->

            when (item.itemId) {

                R.id.nav_home ->
                    cargarFragment(HomeFragment())


                R.id.nav_tareas ->
                    cargarFragment(TareasFragment())

                R.id.nav_chat ->
                    cargarFragment(ChatFragment())

                R.id.nav_perfil ->
                    cargarFragment(PerfilFragment())
            }

            true
        }

        // Drawer lateral
        navView.setNavigationItemSelectedListener { item ->

            when (item.itemId) {

                R.id.nav_dashboard ->
                    cargarFragment(HomeFragment())

                R.id.nav_admin ->
                    cargarFragment(AdminFragment())

                R.id.nav_usuarios ->
                    cargarFragment(UsuariosFragment())

                R.id.nav_empleados ->
                    cargarFragment(EmpleadoFragment())

                R.id.nav_jefes ->
                    cargarFragment(JefeFragment())

                R.id.nav_settings ->
                    Toast.makeText(
                        this,
                        "Configuración próximamente",
                        Toast.LENGTH_SHORT
                    ).show()

                R.id.nav_logout ->
                    cerrarSesion()
            }

            drawerLayout.closeDrawers()

            true
        }
    }

    private fun configurarMenuPorRol(menu: Menu) {

        lifecycleScope.launch {

            val rol = UsuarioRepository.obtenerRolActual()

            runOnUiThread {

                when (rol) {

                    "admin" -> {

                        menu.findItem(R.id.nav_admin).isVisible = true
                        menu.findItem(R.id.nav_usuarios).isVisible = true
                        menu.findItem(R.id.nav_empleados).isVisible = true
                        menu.findItem(R.id.nav_jefes).isVisible = true
                    }

                    "jefe" -> {

                        menu.findItem(R.id.nav_admin).isVisible = false
                        menu.findItem(R.id.nav_usuarios).isVisible = false
                        menu.findItem(R.id.nav_empleados).isVisible = true
                        menu.findItem(R.id.nav_jefes).isVisible = false
                    }

                    "empleado" -> {

                        menu.findItem(R.id.nav_admin).isVisible = false
                        menu.findItem(R.id.nav_usuarios).isVisible = false
                        menu.findItem(R.id.nav_empleados).isVisible = false
                        menu.findItem(R.id.nav_jefes).isVisible = false
                    }

                    else -> {

                        menu.findItem(R.id.nav_admin).isVisible = false
                        menu.findItem(R.id.nav_usuarios).isVisible = false
                        menu.findItem(R.id.nav_empleados).isVisible = false
                        menu.findItem(R.id.nav_jefes).isVisible = false
                    }
                }
            }
        }
    }

    private fun cargarFragment(fragment: Fragment) {

        lifecycleScope.launch {

            val rol =
                UsuarioRepository.obtenerRolActual()

            val permitido = when (rol) {

                "admin" -> true

                "jefe" -> {

                    fragment !is AdminFragment
                }

                "empleado" -> {

                    fragment is HomeFragment ||
                            fragment is PerfilFragment ||
                            fragment is TareasFragment
                }

                else -> false
            }

            runOnUiThread {

                if (permitido) {

                    supportFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.fragment_container,
                            fragment
                        )
                        .commit()

                } else {

                    Toast.makeText(
                        this@MainActivity,
                        "No tienes permisos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun cerrarSesion() {

        lifecycleScope.launch {

            try {

                SupabaseClient.client.auth.signOut()

                CredencialesManager.limpiarCredenciales(this@MainActivity)

                runOnUiThread {

                    startActivity(
                        Intent(
                            this@MainActivity,
                            LoginActivity::class.java
                        )
                    )

                    finishAffinity()
                }

            } catch (e: Exception) {

                runOnUiThread {

                    Toast.makeText(
                        this@MainActivity,
                        "Error al cerrar sesión",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}