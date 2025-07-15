package com.example.fraccionamientoapp.ui.screens.resident

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fraccionamientoapp.R
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CreateInvitationScreen(
    onInvitacionCreada: () -> Unit,
    onCancelar: () -> Unit
) {
    var nombreVisitante by remember { mutableStateOf(TextFieldValue("")) }
    var fecha by remember { mutableStateOf("") }

    val context = LocalContext.current
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val fechaActual = remember { sdf.format(Date()) }

    var mostrarQR by remember { mutableStateOf(false) }
    val qrTexto = "Invitación: ${nombreVisitante.text} - $fecha"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(stringResource(R.string.crear_invitacion_titulo), fontSize = 24.sp)
        Text(stringResource(R.string.app_name))

        OutlinedTextField(
            value = nombreVisitante,
            onValueChange = { nombreVisitante = it },
            label = { Text(stringResource(R.string.nombre_del_visitante)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = fecha,
            onValueChange = { fecha = it },
            label = { Text(stringResource(R.string.fecha_y_hora)) },
            placeholder = { Text(fechaActual) },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (nombreVisitante.text.isNotBlank() && fecha.isNotBlank()) {
                    mostrarQR = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.generar_invitacion))
        }

        if (mostrarQR) {
            stringResource(R.string.invitacion_generada_para)
            Text(qrTexto, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onInvitacionCreada) {
                Text(stringResource(R.string.aceptar))
            }
        }

        OutlinedButton(onClick = onCancelar, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.cancelar))
        }
    }
}

