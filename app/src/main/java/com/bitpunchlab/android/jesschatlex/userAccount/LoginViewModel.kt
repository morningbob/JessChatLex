package com.bitpunchlab.android.jesschatlex.userAccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.saveable
import com.bitpunchlab.android.jesschatlex.awsClient.CognitoClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _emailState = MutableStateFlow<String>("")
    val emailState : StateFlow<String> = _emailState.asStateFlow()

    private val _passwordState = MutableStateFlow<String>("")
    val passwordState : StateFlow<String> = _passwordState.asStateFlow()

    private val _emailErrorState = MutableStateFlow<String>(" ")
    val emailErrorState : StateFlow<String> = _emailErrorState.asStateFlow()

    private val _passwordErrorState = MutableStateFlow<String>(" ")
    val passwordErrorState : StateFlow<String> = _passwordErrorState.asStateFlow()

    private val _readyLogin = MutableStateFlow<Boolean>(false)
    val readyLogin : StateFlow<Boolean> = _readyLogin.asStateFlow()

    private val _shouldNavigateMain = MutableStateFlow<Boolean>(false)
    val shouldNavigateMain : StateFlow<Boolean> = _shouldNavigateMain.asStateFlow()

    private val _shouldNavigateSignUp = MutableStateFlow<Boolean>(false)
    val shouldNavigateSignUp: StateFlow<Boolean> = _shouldNavigateSignUp.asStateFlow()

    private val _loadingAlpha = MutableStateFlow<Float>(0f)
    val loadingAlpha: StateFlow<Float> = _loadingAlpha.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            combine(emailErrorState, passwordErrorState) { email, password ->
                _readyLogin.value = email == "" && password == ""
            }.collect()
        }
    }

    fun updateEmail(inputEmail: String) {
        _emailState.value = inputEmail
        _emailErrorState.value = verifyEmail(inputEmail)
    }

    fun updatePassword(inputPass: String) {
        _passwordState.value = inputPass
        _passwordErrorState.value = verifyPassword(inputPass)
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

    fun loginUser() {
        _loadingAlpha.value = 1f
        CoroutineScope(Dispatchers.IO).launch {
            if (CognitoClient.loginUser(email = emailState.value, password = passwordState.value)) {
                _loadingAlpha.value = 0f
                // navigate to main
                //_shouldNavigateMain.value = true
            } else {
                // display alert
            }
        }
    }
}
/*

var email by savedStateHandle.saveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
        private set
    var password by savedStateHandle.saveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
        private set
    var emailError by savedStateHandle.saveable {
        mutableStateOf("")
    }
    var passwordError by savedStateHandle.saveable {
        mutableStateOf("")
    }
    var loadingAlpha by savedStateHandle.saveable {
        mutableStateOf(0f)
    }
    var shouldNavigateMain by savedStateHandle.saveable {
        mutableStateOf(false)
    }
    var shouldNavigateSignUp by savedStateHandle.saveable {
        mutableStateOf(false)
    }
 */


    //private val _readyRegisterState = MutableStateFlow<Boolean>(false)
    //val readyRegisterState : StateFlow<Boolean> = _readyRegisterState

    //fun updateEmail(inputEmail: String) {
    //    _emailState.value = inputEmail
    //    _emailErrorState.value = verifyEmail(inputEmail)
    //}

    //fun updatePassword(inputPass: String) {
    //    _passwordState.value = inputPass
    //    _passwordErrorState.value = verifyPassword(inputPass)
    //}



/*
class LoginViewModel : ViewModel() {

    private val _emailState = MutableStateFlow<String>("")
    val emailState : StateFlow<String> = _emailState.asStateFlow()

    private val _passwordState = MutableStateFlow<String>("")
    val passwordState : StateFlow<String> = _passwordState.asStateFlow()

    private val _emailErrorState = MutableStateFlow<String>(" ")
    val emailErrorState : StateFlow<String> = _emailErrorState.asStateFlow()

    private val _passwordErrorState = MutableStateFlow<String>(" ")
    val passwordErrorState : StateFlow<String> = _passwordErrorState.asStateFlow()

    //private val _readyRegisterState = MutableStateFlow<Boolean>(false)
    //val readyRegisterState : StateFlow<Boolean> = _readyRegisterState

    fun updateEmail(inputEmail: String) {
        _emailState.value = inputEmail
        _emailErrorState.value = verifyEmail(inputEmail)
    }

    fun updatePassword(inputPass: String) {
        _passwordState.value = inputPass
        _passwordErrorState.value = verifyPassword(inputPass)
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
}

 */