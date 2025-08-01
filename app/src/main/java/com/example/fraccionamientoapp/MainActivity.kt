package com.example.fraccionamientoapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.fraccionamientoapp.auth.FirebaseAuthManager
import com.example.fraccionamientoapp.ui.model.Role
import com.example.fraccionamientoapp.ui.model.User
import com.example.fraccionamientoapp.ui.screens.admin.AdminScreen
import com.example.fraccionamientoapp.ui.screens.admin.EditUserScreen
import com.example.fraccionamientoapp.ui.screens.admin.ManageUsersScreen
import com.example.fraccionamientoapp.ui.screens.admin.ReportsScreen
import com.example.fraccionamientoapp.ui.screens.auth.LoginScreen
import com.example.fraccionamientoapp.ui.screens.auth.RegisterScreen
import com.example.fraccionamientoapp.ui.screens.guard.GuardScreen
import com.example.fraccionamientoapp.ui.screens.resident.*
import com.example.fraccionamientoapp.ui.theme.FraccionamientoAppTheme
import com.example.fraccionamientoapp.ui.viewmodel.UsersViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val usersViewModel: UsersViewModel by viewModels()

        setContent {
            FraccionamientoAppTheme {
                // La Surface debe envolver todo el contenido y sus propiedades se pasan como argumentos.
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Estado para controlar la pantalla visible. Se inicializa en null para mostrar una carga.
                    var activeScreen by remember { mutableStateOf<String?>(null) }
                    var selectedUser by remember { mutableStateOf<User?>(null) }

                    // Efecto que se ejecuta solo una vez para determinar la pantalla inicial.
                    LaunchedEffect(Unit) {
                        if (FirebaseAuthManager.isLoggedIn()) {
                            val userEmail = FirebaseAuthManager.getCurrentUserEmail()
                            // Busca el usuario y su rol en base al email. En una app real, esto vendría de Firestore.
                            val userRole = usersViewModel.users.find { it.email == userEmail }?.role

                            // Asigna la pantalla correcta según el rol encontrado.
                            activeScreen = when (userRole) {
                                Role.ADMIN -> "admin"
                                Role.RESIDENTE -> "resident"
                                Role.GUARDIA -> "guard"
                                else -> "login" // Si hay sesión pero no se encuentra el rol, va a login.
                            }
                        } else {
                            // Si no hay sesión, la pantalla inicial es el login.
                            activeScreen = "login"
                        }
                    }

                    // Mientras se determina la pantalla (`activeScreen` es null), muestra un indicador de carga.
                    if (activeScreen == null) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    } else {
                        // Una vez determinada la pantalla, se muestra el contenido correspondiente.
                        when (activeScreen) {
                            "login" -> {
                                LoginScreen(
                                    onLoginSuccess = {
                                        // Al iniciar sesión, se recalcula la pantalla correcta sin recargar la app.
                                        val userEmail = FirebaseAuthManager.getCurrentUserEmail()
                                        val userRole = usersViewModel.users.find { it.email == userEmail }?.role
                                        activeScreen = when (userRole) {
                                            Role.ADMIN -> "admin"
                                            Role.RESIDENTE -> "resident"
                                            Role.GUARDIA -> "guard"
                                            else -> "login"
                                        }
                                    },
                                    onNavigateToRegister = { activeScreen = "register" },
                                    onPasswordReset = { email ->
                                        FirebaseAuthManager.sendPasswordReset(
                                            email,
                                            onSuccess = {
                                                Toast.makeText(this, "Correo de recuperación enviado", Toast.LENGTH_SHORT).show()
                                            },
                                            onFailure = { error ->
                                                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                    }
                                )
                            }

                            "register" -> {
                                RegisterScreen(onRegisterSuccess = {
                                    activeScreen = "login"
                                    Toast.makeText(this, "Registro exitoso. Inicia sesión.", Toast.LENGTH_SHORT).show()
                                })
                            }

                            // --- PANTALLAS DEL ADMINISTRADOR ---
                            "admin" -> {
                                AdminScreen(
                                    onLogout = {
                                        FirebaseAuthManager.signOut()
                                        activeScreen = "login"
                                    },
                                    onManageUsers = { activeScreen = "admin_users" },
                                    onViewReports = { activeScreen = "admin_reports" }
                                )
                            }
                            "admin_users" -> {
                                ManageUsersScreen(
                                    onBack = { activeScreen = "admin" },
                                    onEditUser = { user ->
                                        selectedUser = user
                                        activeScreen = "edit_user"
                                    }
                                )
                            }
                            "admin_reports" -> {
                                ReportsScreen(onBack = { activeScreen = "admin" })
                            }
                            "edit_user" -> {
                                selectedUser?.let { user ->
                                    EditUserScreen(
                                        user = user,
                                        onSave = { updatedUser ->
                                            usersViewModel.updateUser(updatedUser)
                                            activeScreen = "admin_users"
                                            Toast.makeText(this, "Usuario actualizado", Toast.LENGTH_SHORT).show()
                                        },
                                        onCancel = {
                                            activeScreen = "admin_users"
                                        }
                                    )
                                }
                            }

                            // --- PANTALLAS DEL RESIDENTE ---
                            "resident" -> {
                                ResidentScreen(
                                    onLogout = {
                                        FirebaseAuthManager.signOut()
                                        activeScreen = "login"
                                    },
                                    onCrearInvitacion = { activeScreen = "create_invitation" },
                                    onVerHistorial = { activeScreen = "visitor_history" },
                                    onVehiculos = { activeScreen = "vehicles" },
                                    onEditarPerfil = { activeScreen = "edit_profile" }
                                )
                            }
                            "create_invitation" -> {
                                CreateInvitationScreen(
                                    onInvitacionCreada = {
                                        activeScreen = "resident"
                                        Toast.makeText(this, "Invitación creada con éxito", Toast.LENGTH_SHORT).show()
                                    },
                                    onCancelar = { activeScreen = "resident" }
                                )
                            }
                            "visitor_history" -> {
                                VisitorHistoryScreen(onBack = { activeScreen = "resident" })
                            }
                            "vehicles" -> {
                                VehiclesScreen(onBack = { activeScreen = "resident" })
                            }
                            "edit_profile" -> {
                                EditProfileScreen(onBack = { activeScreen = "resident" })
                            }

                            // --- PANTALLAS DEL GUARDIA ---
                            "guard" -> {
                                GuardScreen(
                                    onLogout = {
                                        FirebaseAuthManager.signOut()
                                        activeScreen = "login"
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
