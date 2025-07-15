package com.example.fraccionamientoapp.ui.screens.resident

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class VisitorEntry(
    val nombre: String,
    val fecha: String,
    val hora: String,
    val estado: String // "Ingresó", "Pendiente", etc.
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisitorHistoryScreen(
    onBack: () -> Unit
) {
    val visitasSimuladas = listOf(
        VisitorEntry("Juan Pérez", "21/05/2025", "14:35", "Ingresó"),
        VisitorEntry("Laura Sánchez", "20/05/2025", "16:20", "Pendiente"),
        VisitorEntry("Carlos Ruiz", "18/05/2025", "12:05", "Ingresó")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de Visitas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(visitasSimuladas) { visita ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Nombre: ${visita.nombre}", fontSize = 18.sp)
                        Text("Fecha: ${visita.fecha}", fontSize = 14.sp)
                        Text("Hora: ${visita.hora}", fontSize = 14.sp)
                        Text("Estado: ${visita.estado}", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}
