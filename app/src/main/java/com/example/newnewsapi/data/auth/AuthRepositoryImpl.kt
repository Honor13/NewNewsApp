package com.example.auth.data.repository

import android.content.SharedPreferences
import com.example.firebasewithmvvm.util.UiState
import com.example.newnewsapi.util.Constants.Companion.AUTH_STATE_KEY
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class AuthRepositoryImpl(
    val auth: FirebaseAuth,
    val appPreferences: SharedPreferences
) : AuthRepository {

    override fun login(email: String, password: String, result: (UiState<String>) -> Unit) {
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{task ->
                    if (task.isSuccessful){
                        appPreferences.edit().putBoolean(AUTH_STATE_KEY,true).apply()

                        result.invoke(
                            UiState.Success("Login successfully!")
                        )


                    } else {
                       result.invoke(
                           UiState.Failure("Login Failed. Please check email and password!")
                       )
                    }

                }
                .addOnFailureListener {
                    result.invoke(
                        UiState.Failure(
                            it.localizedMessage
                        )
                    )
                }

    }

    override fun registerUser(
        name: String,
        email: String,
        password: String,
        result: (UiState<String>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    result.invoke(
                        UiState.Success("User register successfully!")
                    )

                } else {

                    try {
                        throw it.exception ?: java.lang.Exception("Invalid authentication")
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        result.invoke(UiState.Failure("Authentication failed, Password should be at least 6 characters"))
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        result.invoke(UiState.Failure("Authentication failed, Invalid email entered"))
                    } catch (e: FirebaseAuthUserCollisionException) {
                        result.invoke(UiState.Failure("Authentication failed, Email already registered."))
                    } catch (e: Exception) {
                        result.invoke(UiState.Failure(e.message))
                    }
                }


            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun forgotPassword(email: String, result: (UiState<String>) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener{task->
                if (task.isSuccessful) {
                    result.invoke(UiState.Success("Email has been sent"))
                } else {
                    result.invoke(
                        UiState.Failure(
                            task.exception?.message
                        )
                    )
                }

            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        "Authentication failed , check email"
                    )
                )
            }
    }

    override fun logout(result: () -> Unit) {
        auth.signOut()
        result.invoke()
        appPreferences.edit().putBoolean(AUTH_STATE_KEY,false).apply()

    }

    private fun storeSession(){

    }


}