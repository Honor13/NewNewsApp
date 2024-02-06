package com.example.newnewsapi.presentation.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.auth.data.repository.AuthRepository
import com.example.firebasewithmvvm.util.UiState
import com.example.newnewsapi.util.Constants.Companion.AUTH_STATE_KEY
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val repository: AuthRepository,
    val appPreferences: SharedPreferences
) : ViewModel() {

    private val _loginState = MutableLiveData<Boolean>()
    val isLoginAuthVM: LiveData<Boolean>
        get() = _loginState

    private val _register = MutableLiveData<UiState<String>>()
    val register: LiveData<UiState<String>>
        get() = _register

    private val _login = MutableLiveData<UiState<String>>()
    val login: LiveData<UiState<String>>
        get() = _login


    private val _forgotPassword = MutableLiveData<UiState<String>>()
    val forgotPassword: LiveData<UiState<String>>
        get() = _forgotPassword

    private val _logOut = MutableLiveData<UiState<String>>()
    val logOut: LiveData<UiState<String>>
        get() = _logOut


    fun register(
        name: String,
        email: String,
        password: String
    ) {

        _register.value = UiState.Loading
        repository.registerUser(name, email, password) {
            _register.value = it
        }
    }

    fun login(email: String, password: String) {
        _login.value = UiState.Loading
        repository.login(email, password) {
            _login.value = it
        }
    }

    fun forgotPassword(email: String) {
        _forgotPassword.value = UiState.Loading
        repository.forgotPassword(email) {
            _forgotPassword.value = it
        }
    }

    fun logOut(result: () -> Unit) {
        repository.logout(result)
    }

    fun loginState() {
        _loginState.value = appPreferences.getBoolean(AUTH_STATE_KEY, false)
        Log.e("Dante",isLoginAuthVM.value.toString())
    }

}