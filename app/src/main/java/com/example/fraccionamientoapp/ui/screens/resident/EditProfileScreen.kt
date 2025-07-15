package com.example.fraccionamientoapp.ui.screens.resident

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fraccionamientoapp.R
import com.example.fraccionamientoapp.auth.FirebaseAuthManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit
) {
    val currentEmail = FirebaseAuthManager.getCurrentUserEmail() ?: "correo@desconocido.com"
    var nombre by remember { mutableStateOf("Nombre de prueba") } // simulado
    var nuevoNombre by remember { mutableStateOf(nombre) }
    var showUpdateMessage by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.editar_perfil)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(stringResource(R.string.correo_electronico), fontSize = 14.sp)
            OutlinedTextField(
                value = currentEmail,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text(stringResource(R.string.nombre), fontSize = 14.sp)
            OutlinedTextField(
                value = nuevoNombre,
                onValueChange = { nuevoNombre = it },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    nombre = nuevoNombre
                    showUpdateMessage = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.actualizar_nombre))
            }

            if (showUpdateMessage) {
                Text(
                    text = stringResource(R.string.nombre_actualizado),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            OutlinedButton(
                onClick = {
                    FirebaseAuthManager.sendPasswordReset(
                        currentEmail,
                        onSuccess = { showUpdateMessage = true },
                        onFailure = { showUpdateMessage = false }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.cambiar_contrasena))
            }
        }
    }
}
