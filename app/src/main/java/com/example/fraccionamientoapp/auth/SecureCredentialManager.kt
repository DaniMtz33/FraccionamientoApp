package com.example.fraccionamientoapp.auth

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object SecureCredentialManager {

    private const val PREFS_FILE = "secure_auth_prefs"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_PASSWORD = "user_password"

    private fun getEncryptedSharedPreferences(context: Context): EncryptedSharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            PREFS_FILE,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ) as EncryptedSharedPreferences
    }

    // Guarda las credenciales después de un login exitoso con email/contraseña
    fun saveCredentials(context: Context, email: String, password: String) {
        val prefs = getEncryptedSharedPreferences(context)
        with(prefs.edit()) {
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_PASSWORD, password)
            apply()
        }
    }

    // Obtiene las credenciales guardadas. Devuelve null si no hay nada.
    fun getCredentials(context: Context): Pair<String, String>? {
        val prefs = getEncryptedSharedPreferences(context)
        val email = prefs.getString(KEY_USER_EMAIL, null)
        val password = prefs.getString(KEY_USER_PASSWORD, null)

        return if (email != null && password != null) {
            Pair(email, password)
        } else {
            null
        }
    }
}