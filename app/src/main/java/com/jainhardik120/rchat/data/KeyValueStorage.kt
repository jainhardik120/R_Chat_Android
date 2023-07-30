package com.jainhardik120.rchat.data

import android.content.SharedPreferences
import com.jainhardik120.rchat.data.remote.dto.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class KeyValueStorage(
    private val sharedPreferences: SharedPreferences
) {
    private val _isLoggedIn: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val isLoggedIn: Flow<Boolean>
        get() = _isLoggedIn.asStateFlow()

    private val sharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            when (key) {
                TOKEN_KEY -> {
                    _isLoggedIn.value = (sharedPreferences.getString(key, null) != null)
                }
            }
        }


    init {
        _isLoggedIn.value = checkToken()
        startListening()
    }

    private fun startListening() {
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
    }

    fun stopListening() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
    }


    companion object {
        const val TOKEN_KEY = "TOKEN"
        const val EMAIL_KEY = "EMAIL"
        const val USER_ID_KEY = "USER_ID"
    }

     fun getValue(key: String): String? {
         return sharedPreferences.getString(key, null)
     }

    private fun storeValue(key: String, value: String) {
        sharedPreferences.edit()
            .putString(key, value)
            .apply()
    }

    private fun removeValue(key: String) {
        sharedPreferences.edit()
            .remove(key)
            .apply()
    }

    fun clear() {
        sharedPreferences
            .edit()
            .clear()
            .apply()
    }


    fun checkToken(): Boolean {
        return contains(TOKEN_KEY)
    }

    fun getToken(): String? {
        return getValue(TOKEN_KEY)
    }

    fun saveLoginResponse(loginResponse: LoginResponse) {
        storeValue(TOKEN_KEY, loginResponse.token)
        storeValue(EMAIL_KEY, loginResponse.email)
        storeValue(USER_ID_KEY, loginResponse.userId)
    }

    fun contains(key: String): Boolean = sharedPreferences.contains(key)
}