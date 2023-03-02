package com.bitpunchlab.android.jesschatlex.userAccount

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bitpunchlab.android.jesschatlex.awsClient.CognitoClient
import com.bitpunchlab.android.jesschatlex.base.DialogButton
import com.bitpunchlab.android.jesschatlex.helpers.InputValidation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    val _shouldChangePassword = MutableStateFlow<Boolean>(false)
    var shouldChangePassword : StateFlow<Boolean> = _shouldChangePassword.asStateFlow()

    val _currentPassword = MutableStateFlow<String>("")
    var currentPassword : StateFlow<String> = _currentPassword.asStateFlow()

    val _newPassword = MutableStateFlow<String>("")
    var newPassword : StateFlow<String> = _newPassword.asStateFlow()

    val _confirmPassword = MutableStateFlow<String>("")
    var confirmPassword : StateFlow<String> = _confirmPassword.asStateFlow()

    val _currentPassError = MutableStateFlow<String>("")
    var currentPassError : StateFlow<String> = _currentPassError.asStateFlow()

    val _newPassError = MutableStateFlow<String>("")
    var newPassError : StateFlow<String> = _newPassError.asStateFlow()

    val _confirmPassError = MutableStateFlow<String>("")
    var confirmPassError : StateFlow<String> = _confirmPassError.asStateFlow()

    val _changePassResult = MutableStateFlow<Int>(0)
    var changePassResult : StateFlow<Int> = _changePassResult.asStateFlow()

    fun updateShouldChangePassword(newValue: Boolean) {
        _shouldChangePassword.value = newValue
    }

    fun updateCurrentPassword(newValue: String) {
        _currentPassword.value = newValue
        _currentPassError.value = InputValidation.verifyPassword(newValue)
    }

    fun updateNewPassword(newValue: String) {
        _newPassword.value = newValue
        _newPassError.value = InputValidation.verifyPassword(newValue)
    }

    fun updateConfirmPassword(newValue: String) {
        _confirmPassword.value = newValue
        _confirmPassError.value = InputValidation.verifyConfirmPass(newPassword.value, newValue)
    }

    fun updateChangePassResult(newValue: Int) {
        _changePassResult.value = newValue
        //_confirmPassError.value = InputValidation.verifyConfirmPass(newPassword.value, newValue)
    }

    // 1 is succeed, 2 is failed, corresponding dialogs will be displayed
    fun resetPassword(email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            if (CognitoClient.recoverUser(email)) {
                Log.i("reset password", "success")
                // alert user
                _changePassResult.value = 1
            } else {
                // alert user
                _changePassResult.value = 2
            }
        }
    }
}