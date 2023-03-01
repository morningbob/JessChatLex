package com.bitpunchlab.android.jesschatlex.userAccount

import android.util.Log
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

    private val _loadingAlpha = MutableStateFlow<Float>(0f)
    val loadingAlpha: StateFlow<Float> = _loadingAlpha.asStateFlow()

    private val _showFailureDialog = MutableStateFlow<Boolean>(false)
    val showFailureDialog: StateFlow<Boolean> = _showFailureDialog.asStateFlow()

    private val _showForgotDialog = MutableStateFlow<Boolean>(false)
    val showForgotDialog: StateFlow<Boolean> = _showForgotDialog.asStateFlow()

    private val _showConfirmEmailDialog = MutableStateFlow<Boolean>(false)
    val showConfirmEmailDialog: StateFlow<Boolean> = _showConfirmEmailDialog.asStateFlow()

    private val _showConfirmEmailStatus = MutableStateFlow<Int>(0)
    val showConfirmEmailStatus: StateFlow<Int> = _showConfirmEmailStatus.asStateFlow()


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
                // navigate to main
                // setting the alpha here and below, duplicately, because of timing issue
                // want to set to 0f only when login result came back
                _loadingAlpha.value = 0f
            } else {
                // display alert
                Log.i("login user", "failure")
                _showFailureDialog.value = true
                _loadingAlpha.value = 0f
            }

        }
    }

    fun recoverUser(email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            if (CognitoClient.recoverUser(email)) {
                Log.i("password reset", "success")
            } else {
                Log.i("password reset", "error")
            }
        }
    }

    // 0 means not ready, 1 means success, 2 means failure
    fun verifyConfirmCode(email: String, code: String) {
        //_showConfirmEmailDialog.value = true
        Log.i("verify confirm code", "received: $code")
        _loadingAlpha.value = 1f
        CoroutineScope(Dispatchers.IO).launch {
            if (CognitoClient.confirmVerificationCode(email, code)) {
                Log.i("verify code", "success")
                // show success alert
                _loadingAlpha.value = 0f
                _showConfirmEmailStatus.value = 1
            } else {
                Log.i("verify code", "failed")
                _loadingAlpha.value = 0f
                _showConfirmEmailStatus.value = 2
            }
        }
    }

    private fun resetPassword() {
        // show dialog to get email
        // check email exist in Cognito
        //recoverUser()
    }

    fun updateShowDialog(newValue: Boolean) {
        _showFailureDialog.value = newValue
    }

    fun updateShowForgotDialog(newValue: Boolean) {
        _showForgotDialog.value = newValue
    }

    fun updateShowConfirmEmailDialog(newValue: Boolean) {
        _showConfirmEmailDialog.value = newValue
    }

    fun updateConfirmEmailStatus(newValue: Int) {
        _showConfirmEmailStatus.value = newValue
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