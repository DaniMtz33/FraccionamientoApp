package com.example.fraccionamientoapp.ui.screens.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.fraccionamientoapp.R
import com.example.fraccionamientoapp.ui.model.Role
import com.example.fraccionamientoapp.ui.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserScreen(
    user: User,
    onSave: (User) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf(TextFieldValue(user.name)) }
    var expanded by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf(user.role) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.editar_usuario)) },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = stringResource(R.string.volver))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.nombre)) },
                modifier = Modifier.fillMaxWidth()
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = selectedRole.name,
                    onValueChange = {},
                    label = { Text(stringResource(R.string.rol)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true },
                    readOnly = true,
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    Role.entries.forEach { role ->
                        DropdownMenuItem(
                            text = { Text(role.name) },
                            onClick = {
                                selectedRole = role
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val updatedUser = user.copy(
                        name = name.text,
                        role = selectedRole
                    )
                    onSave(updatedUser)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.guardar))
            }

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.cancelar))
            }
        }
    }
}
