package com.example.fraccionamientoapp.auth

import com.google.firebase.auth.FirebaseAuth

object FirebaseAuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Error al iniciar sesión")
            }
    }

    fun signOut() {
        auth.signOut()
    }

    fun getCurrentUserEmail(): String? {
        return auth.currentUser?.email
    }

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun register(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Error al registrar usuario")
            }
    }

    fun sendPasswordReset(
        email: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Error al enviar correo de recuperación.")
            }
    }

    fun traducirErrorAuth(mensajeOriginal: String): String {
        return when {
            mensajeOriginal.contains("The supplied auth credential is incorrect, malformed or has expired.", true) ||
                    mensajeOriginal.contains("does not have a password", true) ->
                "Contraseña incorrecta."

            mensajeOriginal.contains("no user record", true) ||
                    mensajeOriginal.contains("no user", true) ->
                "No existe una cuenta con ese correo."

            mensajeOriginal.contains("network error", true) ->
                "Error de red. Verifica tu conexión."

            mensajeOriginal.contains("email address is badly formatted", true) ->
                "El correo electrónico no es válido."

            mensajeOriginal.contains("too many unsuccessful login attempts", true) ->
                "Demasiados intentos. Intenta más tarde."

            mensajeOriginal.contains("user disabled", true) ->
                "Tu cuenta ha sido desactivada."

            mensajeOriginal.contains("email already in use", true) ->
                "Ese correo ya está registrado."

            mensajeOriginal.contains("weak password", true) ->
                "La contraseña es demasiado débil. Usa al menos 6 caracteres."

            else -> "Ocurrió un error inesperado."
        }
    }

}