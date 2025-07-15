package com.example.fraccionamientoapp.ui.screens.guard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.fraccionamientoapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuardScreen(
    onLogout: () -> Unit
) {
    var query by remember { mutableStateOf(TextFieldValue("")) }

    val accesosSimulados = listOf(
        "Juan Pérez - Invitación válida",
        "Laura Sánchez - Sin registro",
        "Carlos Ruiz - Invitación válida"
    )

    val accesosFiltrados = accesosSimulados.filter {
        it.contains(query.text, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.panel_de_guardia)) },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text(stringResource(R.string.cerrar_sesion), color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text(stringResource(R.string.buscar_visitante)) },
                modifier = Modifier.fillMaxWidth()
            )

            if (accesosFiltrados.isEmpty()) {
                Text(stringResource(R.string.sin_resultados))
            } else {
                accesosFiltrados.forEach { acceso ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(acceso)
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Button(onClick = { /* marcar entrada */ }) {
                                    Text(stringResource(R.string.registrar_entrada))
                                }
                                Button(onClick = { /* marcar salida */ }) {
                                    Text(stringResource(R.string.registrar_salida))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
