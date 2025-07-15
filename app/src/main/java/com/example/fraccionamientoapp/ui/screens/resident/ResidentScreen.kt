package com.example.fraccionamientoapp.ui.screens.resident

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fraccionamientoapp.R
import com.example.fraccionamientoapp.auth.FirebaseAuthManager

@Composable
fun ResidentScreen(
    onLogout: () -> Unit,
    onCrearInvitacion: () -> Unit,
    onVerHistorial: () -> Unit,
    onVehiculos: () -> Unit,
    onEditarPerfil: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Text(stringResource(R.string.panel_del_residente), fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onCrearInvitacion, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.crear_invitacion))
            }

            Button(onClick = onVerHistorial, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.ver_historial_de_visitas))
            }

            Button(onClick = onVehiculos, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.mis_vehiculos))
            }

            Button(onClick = onEditarPerfil, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.editar_perfil))
            }

            Button(
                onClick = {
                    FirebaseAuthManager.signOut()
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(stringResource(R.string.cerrar_sesion), color = MaterialTheme.colorScheme.onError)
            }
        }
    }
}
