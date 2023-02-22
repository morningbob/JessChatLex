package com.bitpunchlab.android.jesschatlex.userAccount

import android.util.Log
import androidx.activity.ComponentActivity
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.kotlin.core.Amplify
import com.bitpunchlab.android.jesschatlex.Login
import com.bitpunchlab.android.jesschatlex.Main
import com.bitpunchlab.android.jesschatlex.R
import com.bitpunchlab.android.jesschatlex.base.*
import kotlinx.coroutines.*

@Composable
fun CreateAccountScreen(navController: NavHostController,
    registerViewModel: RegisterViewModel = viewModel(LocalContext.current as ComponentActivity)) {

    val nameState by registerViewModel.nameState.collectAsState()
    val emailState by registerViewModel.emailState.collectAsState()
    val passwordState by registerViewModel.passwordState.collectAsState()
    val confirmPassState by registerViewModel.confirmPassState.collectAsState()
    val nameErrorState by registerViewModel.nameErrorState.collectAsState()
    val emailErrorState by registerViewModel.emailErrorState.collectAsState()
    val passwordErrorState by registerViewModel.passwordErrorState.collectAsState()
    val confirmPassErrorState by registerViewModel.confirmPassErrorState.collectAsState()

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

                var onSendClicked = {
                    Log.i("onSendClicked", "received")
                    // should validate before sending
                    CoroutineScope(Dispatchers.IO).launch {
                        if (registerUser(nameState, emailState, passwordState)) {
                            // alert user success
                            Log.i("create screen", "success result passed to screen")
                            // in this case, navigate to main screen
                            navController.navigate(Main.route)
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
                    //DescriptionTitleText(title = "Name", paddingTop = 10, paddingBottom = 0)
                    UserInputTextField(title = "Name", content = nameState, hide = false, paddingTop = 10, paddingBottom = 0
                    ) { registerViewModel.updateName(it) }
                    ErrorText(error = nameErrorState)
                    //DescriptionTitleText(title = "Email", paddingTop = 10, paddingBottom = 0)
                    UserInputTextField(title = "Email", content = emailState, hide = false, paddingTop = 10, paddingBottom = 0
                    ) { registerViewModel.updateEmail(it) }
                    ErrorText(error = emailErrorState)
                    //DescriptionTitleText(title = "Password", paddingTop = 10, paddingBottom = 0)
                    UserInputTextField(title = "Password", content = passwordState, hide = true, paddingTop = 10, paddingBottom = 0
                    ) { registerViewModel.updatePassword(it) }
                    ErrorText(error = passwordErrorState)
                    //DescriptionTitleText( title = "Confirm Password", paddingTop = 10, paddingBottom = 0)
                    UserInputTextField(title = "Confirm Password", content = confirmPassState, hide = true,
                        paddingTop = 10, paddingBottom = 0) {
                        registerViewModel.updateConfirmPassword(it)
                    }
                    ErrorText(error = confirmPassErrorState)
                    //DescriptionTitleText(title = , paddingTop = , paddingBottom = )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    GeneralButton(
                        title = "Send",
                        onClick = onSendClicked,
                        shouldEnable = false,
                        paddingTop = 30,
                        paddingBottom = 20
                    )
                    GeneralButton(
                        title = "Login",
                        onClick = onLoginClicked,
                        shouldEnable = true,
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
            .userAttribute(AuthUserAttributeKey.name(), name)
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



