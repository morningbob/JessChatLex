package com.bitpunchlab.android.jesschatlex.userAccount

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavHostController
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.kotlin.core.Amplify
import com.bitpunchlab.android.jesschatlex.Login
import com.bitpunchlab.android.jesschatlex.R
import com.bitpunchlab.android.jesschatlex.base.*
import kotlinx.coroutines.*

@Composable
fun CreateAccountScreen(navController: NavHostController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var nameInput by remember { mutableStateOf(TextFieldValue("")) }
                var emailInput by remember { mutableStateOf(TextFieldValue("")) }
                var passwordInput by remember { mutableStateOf(TextFieldValue("")) }
                var confirmPasswordInput by remember { mutableStateOf(TextFieldValue("")) }
                var onSendClicked = {
                    Log.i("onSendClicked", "received")
                    // should validate before sending
                    CoroutineScope(Dispatchers.IO).launch {
                        if (registerUser(nameInput.text, emailInput.text, passwordInput.text)) {
                            // alert user success
                            Log.i("create screen", "success result passed to screen")
                        } else {
                            // alert user failure
                            Log.i("create screen", "failure result passed to screen")
                        }
                    }
                    Unit
                }
                var onLoginClicked = {
                    navController.navigate(Login.route)
                }

                HeaderImage(
                    resource = R.mipmap.adduser, description = "Create Account logo",
                    paddingTop = 30, paddingBottom = 0
                )
                TitleText(title = "Create Account", paddingTop = 30, paddingBottom = 30)
                Column(horizontalAlignment = Alignment.Start) {
                    DescriptionTitleText(title = "Name", paddingTop = 10, paddingBottom = 0)
                    nameInput = UserInputTextField(hint = "Ben", hide = false)
                    DescriptionTitleText(title = "Email", paddingTop = 10, paddingBottom = 0)
                    emailInput = UserInputTextField(hint = "ben@abc.com", hide = false)
                    DescriptionTitleText(title = "Password", paddingTop = 10, paddingBottom = 0)
                    passwordInput = UserInputTextField(hint = "meqr06px", hide = true)
                    DescriptionTitleText(
                        title = "Confirm Password",
                        paddingTop = 10,
                        paddingBottom = 0
                    )
                    confirmPasswordInput = UserInputTextField(hint = "meqr06px", hide = true)
                    //DescriptionTitleText(title = , paddingTop = , paddingBottom = )
                    GeneralButton(
                        title = "Send",
                        onClick = onSendClicked,
                        paddingTop = 20,
                        paddingBottom = 20
                    )
                    GeneralButton(
                        title = "Login",
                        onClick = onLoginClicked,
                        paddingTop = 10,
                        paddingBottom = 20
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
private suspend fun registerUser(name: String, email: String, password: String) : Boolean =
    suspendCancellableCoroutine<Boolean> { cancellableContinuation ->
        val options = AuthSignUpOptions.builder()
            .userAttribute(AuthUserAttributeKey.email(), email)
            .build()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = Amplify.Auth.signUp(username = email, password = password, options)
                Log.i("AuthQuickStart", "Result: $result")
                cancellableContinuation.resume(true) {}
            } catch (error: AuthException) {
                Log.e("AuthQuickStart", "Sign up failed", error)
                cancellableContinuation.resume(false) {}
            }
    }
}