package com.example.fraccionamientoapp.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.fraccionamientoapp.auth.FirebaseAuthManager
import com.example.fraccionamientoapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    onLogout: () -> Unit,
    onManageUsers: () -> Unit,
    onViewReports: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.panel_admin)) },
                actions = {
                    TextButton(onClick = {
                        FirebaseAuthManager.signOut()
                        onLogout()
                    }) {
                        Text(
                            stringResource(R.string.cerrar_sesion),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onManageUsers, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.gestionar_usuarios))
            }

            Button(onClick = onViewReports, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.ver_reportes))
            }
        }
    }
}

