package com.example.fraccionamientoapp.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.fraccionamientoapp.ui.model.Role
import com.example.fraccionamientoapp.ui.model.User

class UsersViewModel : ViewModel() {

    private val _users = mutableStateListOf(
        User(1, "Daniel Martínez", "admin@fracc.com", Role.ADMIN),
        User(2, "Carlos Vigilante", "guardia1@fracc.com", Role.GUARDIA),
        User(3, "María López", "maria@gmail.com", Role.RESIDENTE)
    )

    val users: List<User> get() = _users

    fun updateUser(updatedUser: User) {
        val index = _users.indexOfFirst { it.id == updatedUser.id }
        if (index != -1) {
            _users[index] = updatedUser
        }
    }

    fun getUserById(id: Int): User? {
        return _users.find { it.id == id }
    }
}
