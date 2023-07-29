package com.jainhardik120.rchat.ui.presentation.screens.login

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jainhardik120.rchat.Result
import com.jainhardik120.rchat.data.remote.RChatApi
import com.jainhardik120.rchat.data.remote.dto.LoginRequest
import com.jainhardik120.rchat.data.remote.dto.MessageError
import com.jainhardik120.rchat.data.remote.dto.SignupRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val api: RChatApi
) : ViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    private val _loginState = mutableStateOf(LoginState())
    val loginState: State<LoginState> = _loginState


    private fun <T, R> makeApiCall(
        call: suspend () -> Result<T, R>,
        preExecuting: (() -> Unit)? = {
            _loginState.value = _loginState.value.copy(loading = true)
        },
        onDoneExecuting: (() -> Unit)? = {
            _loginState.value = _loginState.value.copy(loading = false)
        },
        onException: (String) -> Unit = { errorMessage ->
            Log.d(TAG, "makeApiCall: $errorMessage")
        },
        onError: (R) -> Unit = { errorBody ->
            if (errorBody is MessageError) {
                onException(errorBody.error)
            }
        },
        onSuccess: (T) -> Unit
    ) {
        viewModelScope.launch {
            preExecuting?.invoke()
            val result = call.invoke()
            onDoneExecuting?.invoke()
            when (result) {
                is Result.ClientException -> {
                    result.errorBody?.let(onError)
                }

                is Result.Exception -> {
                    result.errorMessage?.let(onException)
                }

                is Result.Success -> {
                    result.data?.let(onSuccess)
                }
            }
        }
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
                }) {
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
                }) {
                    api.saveLoginResponse(it)
                }
            }
        }
    }
}

data class LoginState(
    val loginEmail: String = "",
    val loginPassword: String = "",
    val registerMail: String = "",
    val registerPassword: String = "",
    val registerUsername: String = "",
    val loading: Boolean = false
)


sealed class LoginEvent {
    data class LoginEmailChanged(val email: String) : LoginEvent()
    data class LoginPasswordChanged(val password: String) : LoginEvent()
    data class RegisterMailChanged(val email: String) : LoginEvent()
    data class RegisterPasswordChanged(val password: String) : LoginEvent()
    data class RegisterUsernameChanged(val username: String) : LoginEvent()
    object LoginButtonClicked : LoginEvent()
    object RegisterButtonClicked : LoginEvent()
}