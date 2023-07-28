package com.jainhardik120.rchat

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.jainhardik120.rchat.data.remote.RChatApi
import com.jainhardik120.rchat.data.remote.dto.LoginRequest
import com.jainhardik120.rchat.ui.theme.RChatTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @Inject
    lateinit var service : RChatApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RChatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        val scope = rememberCoroutineScope()
                        var email by remember { mutableStateOf("") }
                        var password by remember { mutableStateOf("") }
                        TextField(value = email, onValueChange = { email = it })
                        TextField(value = password, onValueChange = { password = it })
                        Button(onClick = {
                            scope.launch {
                                when (val response = service.createGroup(email)) {
                                    is Result.ClientException -> {
                                        Log.d(TAG, "ClientException: ${response.errorBody?.error}")
                                    }

                                    is Result.Exception -> {
                                        Log.d(TAG, "Exception: ${response.errorMessage}")
                                    }

                                    is Result.Success -> {
                                        Log.d(TAG, "Success: ${(response.data)}")
                                    }
                                }
                            }
                        }) {
                            Text(text = "Login")
                        }
                    }
                }
            }
        }
    }
}
