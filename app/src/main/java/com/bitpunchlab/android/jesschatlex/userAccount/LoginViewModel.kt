package com.bitpunchlab.android.jesschatlex.userAccount

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {
    private val _emailState = MutableStateFlow<String>("")
    val emailState : StateFlow<String> = _emailState.asStateFlow()

    private val _passwordState = MutableStateFlow<String>("")
    val passwordState : StateFlow<String> = _passwordState.asStateFlow()

    fun updateEmail(inputEmail: String) {
        //email = inputEmail
        //verifyEmail(email)
        _emailState.value = inputEmail
    }

    fun updatePassword(inputPass: String) {
        //password = inputPass
        _passwordState.value = inputPass
    }
}