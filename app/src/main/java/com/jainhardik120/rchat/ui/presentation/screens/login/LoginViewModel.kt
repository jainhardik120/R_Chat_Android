package com.jainhardik120.rchat.ui.presentation.screens.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.jainhardik120.rchat.data.remote.RChatApi
import com.jainhardik120.rchat.data.remote.dto.LoginRequest
import com.jainhardik120.rchat.data.remote.dto.SignupRequest
import com.jainhardik120.rchat.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val api: RChatApi
) : BaseViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    private val _loginState = mutableStateOf(LoginState())
    val loginState: State<LoginState> = _loginState

    private fun onLoadingStart() {
        _loginState.value = _loginState.value.copy(loading = true)
    }

    private fun onLoadingStop() {
        _loginState.value = _loginState.value.copy(loading = false)
    }

    fun onLoginEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.LoginEmailChanged -> {
                _loginState.value = _loginState.value.copy(loginEmail = event.email)
            }

            is LoginEvent.LoginPasswordChanged -> {
                _loginState.value = _loginState.value.copy(loginPassword = event.password)
            }

            is LoginEvent.RegisterMailChanged -> {
                _loginState.value = _loginState.value.copy(registerMail = event.email)
            }

            is LoginEvent.RegisterPasswordChanged -> {
                _loginState.value = _loginState.value.copy(registerPassword = event.password)
            }

            is LoginEvent.RegisterUsernameChanged -> {
                _loginState.value = _loginState.value.copy(registerUsername = event.username)
            }

            LoginEvent.LoginButtonClicked -> {
                makeApiCall(call = {
                    api.login(
                        LoginRequest(
                            email = _loginState.value.loginEmail,
                            password = _loginState.value.loginPassword
                        )
                    )
                }, preExecuting = { onLoadingStart() }, onDoneExecuting = { onLoadingStop() }) {
                    api.saveLoginResponse(it)
                }
            }

            LoginEvent.RegisterButtonClicked -> {
                makeApiCall(call = {
                    api.signup(
                        SignupRequest(
                            email = _loginState.value.registerMail,
                            password = _loginState.value.registerPassword,
                            username = _loginState.value.registerUsername
                        )
                    )
                }, preExecuting = { onLoadingStart() }, onDoneExecuting = { onLoadingStop() }) {
                    api.saveLoginResponse(it)
                }
            }
        }
    }
}


