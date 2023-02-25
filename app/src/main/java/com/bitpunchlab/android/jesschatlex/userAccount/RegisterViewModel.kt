package com.bitpunchlab.android.jesschatlex.userAccount

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.saveable
import com.bitpunchlab.android.jesschatlex.awsClient.CognitoClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class RegisterViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _nameState = MutableStateFlow<String>("")
    val nameState : StateFlow<String> = _nameState.asStateFlow()

    private val _emailState = MutableStateFlow<String>("")
    val emailState : StateFlow<String> = _emailState.asStateFlow()

    private val _passwordState = MutableStateFlow<String>("")
    val passwordState : StateFlow<String> = _passwordState.asStateFlow()

    private val _confirmPassState = MutableStateFlow<String>("")
    val confirmPassState : StateFlow<String> = _confirmPassState.asStateFlow()

    // default the errors with space, that distinguish when the app starts,
    // shouldn't let send button enabled.

    private val _nameErrorState = MutableStateFlow<String>(" ")
    val nameErrorState : StateFlow<String> = _nameErrorState.asStateFlow()

    private val _emailErrorState = MutableStateFlow<String>(" ")
    val emailErrorState : StateFlow<String> = _emailErrorState.asStateFlow()

    private val _passwordErrorState = MutableStateFlow<String>(" ")
    val passwordErrorState : StateFlow<String> = _passwordErrorState.asStateFlow()

    private val _confirmPassErrorState = MutableStateFlow<String>(" ")
    val confirmPassErrorState : StateFlow<String> = _confirmPassErrorState.asStateFlow()

    var readyRegister = nameErrorState.value == "" && emailErrorState.value == ""
            && passwordErrorState.value == "" && confirmPassErrorState.value == ""

    private val _shouldNavigateMain = MutableStateFlow<Boolean>(false)
    val shouldNavigateMain : StateFlow<Boolean> = _shouldNavigateMain.asStateFlow()

    private val _shouldNavigateLogin = MutableStateFlow<Boolean>(false)
    val shouldNavigateLogin: StateFlow<Boolean> = _shouldNavigateLogin.asStateFlow()

    private val _loadingAlpha = MutableStateFlow<Float>(0f)
    val loadingAlpha: StateFlow<Float> = _loadingAlpha.asStateFlow()

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

    private fun verifyPassword(inputPassword: String) : String {
        if (inputPassword.length < 8) return "Password must contain at least 8 characters."
        if (inputPassword.filter { !it.isLetter() }.firstOrNull() == null)
            return "Password must contain a special character or a number."
        return ""
    }

    private fun verifyConfirmPass(password: String, confirmPassword: String) : String {
        if (password != confirmPassword) {
            return "Password and confirm password must be the same."
        }
        return ""
    }

    fun registerUser() {
        _loadingAlpha.value = 1f
        CoroutineScope(Dispatchers.IO).launch {
            if (CognitoClient.registerUser(nameState.value, emailState.value, passwordState.value)) {
                _loadingAlpha.value = 0f
            }
        }
    }

    fun navigateLogin() {
        _shouldNavigateLogin.value = true
    }
}
    /*
    var name by savedStateHandle.saveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var email by savedStateHandle.saveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var password by savedStateHandle.saveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var confirmPass by savedStateHandle.saveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    var nameError by savedStateHandle.saveable {
        mutableStateOf("")
    }
    var emailError by savedStateHandle.saveable {
        mutableStateOf("")
    }
    var passwordError by savedStateHandle.saveable {
        mutableStateOf("")
    }
    var confirmPassError by savedStateHandle.saveable {
        mutableStateOf("")
    }
    var shouldNavigateMain by savedStateHandle.saveable {
        mutableStateOf(false)
    }
    var shouldNavigateLogin by savedStateHandle.saveable {
        mutableStateOf(false)
    }
    var loadingAlpha by savedStateHandle.saveable {
        mutableStateOf(0f)
    }
*/

/*
class RegisterViewModel : ViewModel() {

    private val _nameState = MutableStateFlow<String>("")
    val nameState : StateFlow<String> = _nameState.asStateFlow()

    private val _emailState = MutableStateFlow<String>("")
    val emailState : StateFlow<String> = _emailState.asStateFlow()

    private val _passwordState = MutableStateFlow<String>("")
    val passwordState : StateFlow<String> = _passwordState.asStateFlow()

    private val _confirmPassState = MutableStateFlow<String>("")
    val confirmPassState : StateFlow<String> = _confirmPassState.asStateFlow()

    // default the errors with space, that distinguish when the app starts,
    // shouldn't let send button enabled.

    private val _nameErrorState = MutableStateFlow<String>(" ")
    val nameErrorState : StateFlow<String> = _nameErrorState.asStateFlow()

    private val _emailErrorState = MutableStateFlow<String>(" ")
    val emailErrorState : StateFlow<String> = _emailErrorState.asStateFlow()

    private val _passwordErrorState = MutableStateFlow<String>(" ")
    val passwordErrorState : StateFlow<String> = _passwordErrorState.asStateFlow()

    private val _confirmPassErrorState = MutableStateFlow<String>(" ")
    val confirmPassErrorState : StateFlow<String> = _confirmPassErrorState.asStateFlow()

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

 */