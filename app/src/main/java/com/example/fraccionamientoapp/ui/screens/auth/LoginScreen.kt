package com.example.fraccionamientoapp.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fraccionamientoapp.R
import com.example.fraccionamientoapp.auth.FirebaseAuthManager

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onPasswordReset: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.inicio_de_sesion), fontSize = 28.sp)
            Spacer(modifier = Modifier.height(80.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(R.string.correo_electronico)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(stringResource(R.string.contrasena)) },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = icon, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (email.isNotBlank() && password.isNotBlank()) {
                            FirebaseAuthManager.signIn(
                                email = email,
                                password = password,
                                onSuccess = {
                                    showError = false
                                    onLoginSuccess()
                                },
                                onFailure = { error ->
                                    showError = true
                                    //Log.e("LoginError", error)
                                    errorMessage = FirebaseAuthManager.traducirErrorAuth(error)
                                }

                            )
                        } else {
                            showError = true
                            errorMessage = "Completa todos los campos."
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.iniciar_sesion))
                }

                if (showError) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = {
                    if (email.isNotBlank()) {
                        onPasswordReset(email)
                    } else {
                        errorMessage = "Ingresa tu correo para recuperar tu contraseña."
                        showError = true
                    }
                }) {
                    Text(stringResource(R.string.olvidaste_tu_contrasena))
                }

                TextButton(onClick = {
                    onNavigateToRegister()
                }) {
                    Text(stringResource(R.string.no_tienes_cuenta_registrate))
                }
            }
        }
    }
}
