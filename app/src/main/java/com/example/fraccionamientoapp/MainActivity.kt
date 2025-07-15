package com.example.fraccionamientoapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fraccionamientoapp.auth.FirebaseAuthManager
import com.example.fraccionamientoapp.ui.model.User
import com.example.fraccionamientoapp.ui.screens.admin.AdminScreen
import com.example.fraccionamientoapp.ui.screens.resident.CreateInvitationScreen
import com.example.fraccionamientoapp.ui.screens.resident.EditProfileScreen
import com.example.fraccionamientoapp.ui.screens.admin.EditUserScreen
import com.example.fraccionamientoapp.ui.screens.auth.LoginScreen
import com.example.fraccionamientoapp.ui.screens.admin.ManageUsersScreen
import com.example.fraccionamientoapp.ui.screens.auth.RegisterScreen
import com.example.fraccionamientoapp.ui.screens.admin.ReportsScreen
import com.example.fraccionamientoapp.ui.screens.resident.ResidentScreen
import com.example.fraccionamientoapp.ui.screens.resident.VehiclesScreen
import com.example.fraccionamientoapp.ui.screens.resident.VisitorHistoryScreen
import com.example.fraccionamientoapp.ui.theme.FraccionamientoAppTheme
import com.example.fraccionamientoapp.ui.viewmodel.UsersViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val usersViewModel: UsersViewModel by viewModels()

        setContent {
            FraccionamientoAppTheme {
                var selectedUser by remember { mutableStateOf<User?>(null) }
                var isLoggedIn by remember { mutableStateOf(false) }
                var isRegistering by remember { mutableStateOf(false) }
                var activeScreen by remember { mutableStateOf("login") }

                when {
                    isRegistering -> {
                        RegisterScreen(onRegisterSuccess = {
                            isRegistering = false
                            Toast.makeText(
                                this,
                                "Registro exitoso. Inicia sesión.",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                    }

                    !isLoggedIn -> {
                        LoginScreen(
                            onLoginSuccess = {
                                isLoggedIn = true
                                activeScreen = "admin"
                            },
                            onNavigateToRegister = { isRegistering = true },
                            onPasswordReset = { email ->
                                FirebaseAuthManager.sendPasswordReset(
                                    email,
                                    onSuccess = {
                                        Toast.makeText(
                                            this,
                                            "Correo de recuperación enviado",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    onFailure = { error ->
                                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        )
                    }

                    activeScreen == "create_invitation" -> {
                        CreateInvitationScreen(
                            onInvitacionCreada = {
                                activeScreen = "resident"
                                Toast.makeText(
                                    this,
                                    "Invitación creada con éxito",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onCancelar = {
                                activeScreen = "resident"
                            }
                        )
                    }

                    activeScreen == "resident" -> {
                        ResidentScreen(
                            onLogout = {
                                isLoggedIn = false
                                activeScreen = "login"
                            },
                            onCrearInvitacion = {
                                activeScreen = "create_invitation"
                            },
                            onVerHistorial = {
                                activeScreen = "visitor_history"
                            },
                            onVehiculos = {
                                activeScreen = "vehicles"
                            },
                            onEditarPerfil = {
                                activeScreen = "edit_profile"
                            }
                        )
                    }

                    activeScreen == "visitor_history" -> {
                        VisitorHistoryScreen(onBack = { activeScreen = "resident" })
                    }

                    activeScreen == "vehicles" -> {
                        VehiclesScreen(onBack = { activeScreen = "resident" })
                    }

                    activeScreen == "edit_profile" -> {
                        EditProfileScreen(onBack = { activeScreen = "resident" })
                    }

                    activeScreen == "admin" -> {
                        AdminScreen(
                            onLogout = {
                                isLoggedIn = false
                                activeScreen = "login"
                            },
                            onManageUsers = {
                                activeScreen = "admin_users"
                            },
                            onViewReports = {
                                activeScreen = "admin_reports"
                            }
                        )
                    }

                    activeScreen == "admin_users" -> {
                        ManageUsersScreen(
                            onBack = { activeScreen = "admin" },
                            onEditUser = { user ->
                                selectedUser = user
                                activeScreen = "edit_user"
                            }
                        )
                    }

                    activeScreen == "admin_reports" -> {
                        ReportsScreen(onBack = { activeScreen = "admin" })
                    }

                    activeScreen == "edit_user" -> {
                        selectedUser?.let { user ->
                            EditUserScreen(
                                user = user,
                                onSave = { updatedUser ->
                                    // Aquí podrías actualizar la base o lista más adelante
                                    activeScreen = "admin_users"
                                    Toast.makeText(this, "Usuario actualizado", Toast.LENGTH_SHORT)
                                        .show()
                                },
                                onCancel = {
                                    activeScreen = "admin_users"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}




