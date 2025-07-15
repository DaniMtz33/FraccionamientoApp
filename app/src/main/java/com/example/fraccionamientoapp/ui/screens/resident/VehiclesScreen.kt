package com.example.fraccionamientoapp.ui.screens.resident

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fraccionamientoapp.R

data class Vehicle(
    val placa: String,
    val marca: String,
    val modelo: String,
    val color: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiclesScreen(
    onBack: () -> Unit
) {
    var placa by remember { mutableStateOf("") }
    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }

    var vehicles by remember { mutableStateOf(listOf<Vehicle>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.mis_vehiculos)) },
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
            OutlinedTextField(
                value = placa,
                onValueChange = { placa = it },
                label = { Text(stringResource(R.string.placa)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = marca,
                onValueChange = { marca = it },
                label = { Text(stringResource(R.string.marca)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = modelo,
                onValueChange = { modelo = it },
                label = { Text(stringResource(R.string.modelo)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = color,
                onValueChange = { color = it },
                label = { Text(stringResource(R.string.color)) },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (placa.isNotBlank() && marca.isNotBlank() && modelo.isNotBlank() && color.isNotBlank()) {
                        vehicles = vehicles + Vehicle(placa, marca, modelo, color)
                        // limpiar campos
                        placa = ""
                        marca = ""
                        modelo = ""
                        color = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.registrar_vehiculo))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(stringResource(R.string.vehiculos_registrados), fontSize = 18.sp)

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(vehicles) { vehicle ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("${stringResource(R.string.placa)}: ${vehicle.placa}")
                            Text("${stringResource(R.string.marca)}: ${vehicle.marca}")
                            Text("${stringResource(R.string.modelo)}: ${vehicle.modelo}")
                            Text("${stringResource(R.string.color)}: ${vehicle.color}")
                        }
                    }
                }
            }
        }
    }
}
