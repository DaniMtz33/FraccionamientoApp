package com.example.fraccionamientoapp.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.fraccionamientoapp.R
import com.example.fraccionamientoapp.ui.model.Role
import com.example.fraccionamientoapp.ui.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageUsersScreen(
    onBack: () -> Unit,
    onEditUser: (User) -> Unit
) {
    val usuarios = listOf(
        User(1, "Daniel Martínez", "admin@fracc.com", Role.ADMIN),
        User(2, "Carlos Vigilante", "guardia1@fracc.com", Role.GUARDIA),
        User(3, "María López", "maria@gmail.com", Role.RESIDENTE)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.gestionar_usuarios)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.volver))
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(usuarios) { usuario ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("${stringResource(R.string.nombre)}: ${usuario.name}")
                        Text("${stringResource(R.string.correo_electronico)}: ${usuario.email}")
                        Text("${stringResource(R.string.rol)}: ${usuario.role.name}")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { onEditUser(usuario) },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(stringResource(R.string.editar))
                        }
                    }
                }
            }
        }
    }
}

