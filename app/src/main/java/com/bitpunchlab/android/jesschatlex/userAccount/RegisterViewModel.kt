package com.bitpunchlab.android.jesschatlex.userAccount

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class RegisterViewModel : ViewModel() {

    private val _nameState = MutableStateFlow<String>("")
    val nameState : StateFlow<String> = _nameState.asStateFlow()

    private val _emailState = MutableStateFlow<String>("")
    val emailState : StateFlow<String> = _emailState.asStateFlow()

    private val _passwordState = MutableStateFlow<String>("")
    val passwordState : StateFlow<String> = _passwordState.asStateFlow()

    private val _confirmPassState = MutableStateFlow<String>("")
    val confirmPassState : StateFlow<String> = _confirmPassState.asStateFlow()

    private val _nameErrorState = MutableStateFlow<String>("")
    val nameErrorState : StateFlow<String> = _confirmPassState.asStateFlow()

    private val _emailErrorState = MutableStateFlow<String>("")
    val emailErrorState : StateFlow<String> = _confirmPassState.asStateFlow()

    private val _passwordErrorState = MutableStateFlow<String>("")
    val passwordErrorState : StateFlow<String> = _confirmPassState.asStateFlow()

    private val _confirmPassErrorState = MutableStateFlow<String>("")
    val confirmPassErrorState : StateFlow<String> = _confirmPassState.asStateFlow()

    fun updateName(inputName: String) {
        _nameState.value = inputName
        _nameErrorState.value = verifyName(inputName)
    }

    fun updateEmail(inputEmail: String) {
        _emailState.value = inputEmail
        _emailErrorState.value = verifyEmail(inputEmail)
    }

    fun updatePassword(inputPass: String) {
        _passwordState.value = inputPass
        _passwordErrorState.value = verifyPassword(inputPass)
    }

    fun updateConfirmPassword(inputConfirm: String) {
        _confirmPassState.value = inputConfirm
        _confirmPassErrorState.value = verifyConfirmPass(passwordState.value, inputConfirm)
    }

    private fun verifyName(inputName: String) : String {
        return when (inputName) {
            "" -> {
                "Name must not be blank."
            }
            else -> {
                ""
            }
        }
    }

    private fun verifyEmail(inputEmail: String) : String {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()) {
            return "Email is invalid."
        }
        return ""
    }

    //private fun verifyPassword(inputPassword: String) : String {
     //   return  getPasswordError(inputPassword)
    //}

    private fun verifyPassword(inputPassword: String) : String {
        if (inputPassword.length < 8) return "Password must contain at least 8 characters."
        //if (inputPassword.filter { it.isDigit() }.firstOrNull() == null) return "false"
        //if (inputPassword.filter { it.isLetter() }.filter { it.isUpperCase() }.firstOrNull() == null) return false
        //if (inputPassword.filter { it.isLetter() }.filter { it.isLowerCase() }.firstOrNull() == null) return false
        if (inputPassword.filter { !it.isLetter() }.firstOrNull() == null)
            return "Password must contain a special character or a number."
        return ""
    }

    //private fun verifyConfirmPass(inputConfirmPass: String) : String {
        // check with password everytime
    //    _confirmPassErrorState.value = checkPasswords(passwordState.value, inputConfirmPass)
    //}

    private fun verifyConfirmPass(password: String, confirmPassword: String) : String {
        if (password != confirmPassword) {
            return "Password and confirm password must be the same."
        }
        return ""
    }

}