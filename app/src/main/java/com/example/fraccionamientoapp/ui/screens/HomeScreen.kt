package com.example.fraccionamientoapp.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.fraccionamientoapp.ui.model.Role
import com.example.fraccionamientoapp.ui.screens.admin.AdminScreen
import com.example.fraccionamientoapp.ui.screens.guard.GuardScreen
import com.example.fraccionamientoapp.ui.screens.resident.ResidentScreen

@Composable
fun HomeScreen() {
    // Simulación de rol actual del usuario
    val simulatedRole = remember { mutableStateOf(Role.ADMIN) }

    when (simulatedRole.value) {
        Role.ADMIN -> AdminScreen(
            onLogout = { /* TODO: implementar logout */ },
            onManageUsers = { /* TODO: ir a pantalla de usuarios */ },
            onViewReports = { /* TODO: ir a pantalla de reportes */ }
        )
        Role.GUARDIA -> GuardScreen(
            onLogout = { /* TODO: implementar logout */ },
        )
        Role.RESIDENTE -> ResidentScreen(
            onLogout = { TODO("Implement logout") },
            onCrearInvitacion = { TODO("Implement create invitation") },
            onVerHistorial = { TODO("Implement view history") },
            onVehiculos = { TODO("Implement vehicles") },
            onEditarPerfil = { TODO("Implement edit profile") }
        )

        Role.VISITANTE -> Text("Los visitantes no usan la app directamente.")
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeScreen()
}
