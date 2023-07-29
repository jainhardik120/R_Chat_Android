package com.jainhardik120.rchat.ui.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jainhardik120.rchat.data.remote.ChatSocketService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val socketService: ChatSocketService
) : ViewModel() {

    init {
        connect()
    }

    private fun connect() {
        viewModelScope.launch {
            socketService.init()
        }
    }

    private fun disconnect() {
        viewModelScope.launch {
            socketService.close()
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}