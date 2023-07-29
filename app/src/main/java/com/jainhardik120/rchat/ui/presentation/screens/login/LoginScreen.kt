package com.jainhardik120.rchat.ui.presentation.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen() {
    val viewModel = hiltViewModel<LoginViewModel>()
    val navController = rememberNavController()

    Scaffold {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            NavHost(navController, startDestination = "signIn", modifier = Modifier.padding(it)) {
                composable("signIn") {
                    SignInScreen(
                        navController = navController,
                        state = viewModel.loginState.value,
                        onEvent = viewModel::onLoginEvent
                    )
                }
                composable("signUp") {
                    SignUpScreen(
                        navController = navController,
                        loginState = viewModel.loginState.value,
                        onEvent = viewModel::onLoginEvent
                    )
                }
            }
            if (viewModel.loginState.value.loading) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun SignInScreen(
    navController: NavHostController,
    state: LoginState,
    onEvent: (LoginEvent) -> Unit
) {
    Column {
        OutlinedTextField(
            value = state.loginEmail,
            onValueChange = { onEvent(LoginEvent.LoginEmailChanged(it)) })
        OutlinedTextField(
            value = state.loginPassword,
            onValueChange = { onEvent(LoginEvent.LoginPasswordChanged(it)) })
        Button(onClick = { onEvent(LoginEvent.LoginButtonClicked) }) {
            Text(text = "Login")
        }
        Button(onClick = { navController.navigate("signUp") }) {
            Text(text = "SignUp")
        }
    }
}

@Composable
fun SignUpScreen(
    navController: NavHostController,
    loginState: LoginState,
    onEvent: (LoginEvent) -> Unit
) {
    var showPassword by rememberSaveable { mutableStateOf(false) }
    Column {
        OutlinedTextField(
            value = loginState.registerMail,
            onValueChange = { onEvent(LoginEvent.RegisterMailChanged(it)) },
            label = { Text(text = "Email") },
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(onNext = {

            }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = loginState.registerPassword,
            onValueChange = { onEvent(LoginEvent.RegisterPasswordChanged(it)) },
            label = { Text(text = "Password") },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(onNext = {

            }),
            trailingIcon = {
                if (showPassword) {
                    IconButton(onClick = { showPassword = false }) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = "Hide Password"
                        )
                    }
                } else {
                    IconButton(onClick = { showPassword = true }) {
                        Icon(
                            imageVector = Icons.Filled.VisibilityOff,
                            contentDescription = "Show Password"
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = loginState.registerUsername,
            onValueChange = { onEvent(LoginEvent.RegisterUsernameChanged(it)) },
            label = { Text(text = "Username") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {

            }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Button(
            onClick = { onEvent(LoginEvent.RegisterButtonClicked) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Register")
        }
    }
}