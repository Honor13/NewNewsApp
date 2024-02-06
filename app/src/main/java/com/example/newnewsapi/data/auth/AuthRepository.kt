package com.example.auth.data.repository

import com.example.firebasewithmvvm.util.UiState

interface AuthRepository {

    fun login(email: String, password: String, result: (UiState<String>) -> Unit)
    fun registerUser(name: String, email: String, password: String, result: (UiState<String>) -> Unit)
    fun forgotPassword(email: String, result: (UiState<String>) -> Unit)
    fun logout(result: () -> Unit)

}