package com.bitpunchlab.android.jesschatlex.userAccount

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.cognito.options.AWSCognitoAuthSignInOptions
import com.amplifyframework.auth.cognito.options.AuthFlowType
import com.amplifyframework.auth.options.AuthSignInOptions
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.kotlin.core.Amplify
import com.bitpunchlab.android.jesschatlex.CreateAccount
import com.bitpunchlab.android.jesschatlex.R
import com.bitpunchlab.android.jesschatlex.base.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.math.log

@Composable
fun LoginScreen(navController: NavHostController,
    loginViewModel: LoginViewModel = viewModel()) {

    val emailState by loginViewModel.emailState.collectAsState()
    val passwordState by loginViewModel.passwordState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        var emailInput by remember { mutableStateOf(TextFieldValue("")) }
        var passwordInput by remember { mutableStateOf(TextFieldValue("")) }
        var onSendClicked = {
            //Log.i("onSendClicked", "received in main")
            CoroutineScope(Dispatchers.IO).launch {
                if (loginUser(emailInput.text, passwordInput.text)) {
                    Log.i("login screen", "success passed to screen")
                } else {
                    Log.i("login screen", "failure passed to screen")
                }
            }
            Unit
        }
        var onSignUpClicked = {
            navController.navigate(CreateAccount.route)
            Log.i("onSignUpClicked", "received in main")
            Unit
        }
        val printInputs = {
            Log.i("email", emailInput.text)
            Unit
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                HeaderImage(resource = R.mipmap.login, description = "Login Icon",
                paddingTop = 0, paddingBottom = 0)
                TitleText(title = "Login", paddingTop = 30, paddingBottom = 30)
            }
            Column(horizontalAlignment = Alignment.Start) {
                //DescriptionTitleText(title = "Email", paddingTop = 10, paddingBottom = 0)
                UserInputTextField(title = "Email", content = emailState, hide = false, paddingTop = 10, paddingBottom = 0
                ) { loginViewModel.updateEmail(it) }
                //DescriptionTitleText(title = "Password", paddingTop = 10, paddingBottom = 0)
                UserInputTextField(title = "Password", content = passwordState, hide = true, paddingTop = 10, paddingBottom = 0
                ) { loginViewModel.updatePassword(it) }
            }
            GeneralButton(title = "Send", onClick = onSendClicked, paddingTop = 30, paddingBottom = 0)
            GeneralButton(title = "Sign Up", onClick = onSignUpClicked, paddingTop = 10, paddingBottom = 20)
            GeneralButton(title = "test", onClick = printInputs, paddingTop = 10, paddingBottom = 10)
        }
    }
}

private suspend fun loginUser(email: String, password: String) : Boolean =
    suspendCancellableCoroutine<Boolean> { cancellableContinuation ->
        val map = HashMap<String, String>()
        map.put("email", email)
        val options = AWSCognitoAuthSignInOptions.builder()
            //.authFlowType(AuthFlowType.USER_PASSWORD_AUTH)
            .metadata(map)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = Amplify.Auth.signIn(username = email, password = password)
                if (result.isSignedIn) {
                    Log.i("AuthQuickstart", "Sign in succeeded")
                } else {
                    Log.e("AuthQuickstart", "Sign in not complete")
                }
            } catch (error: AuthException) {
                Log.e("AuthQuickstart", "Sign in failed", error)
            }
        }

}


/*
TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, start = 30.dp, end = 30.dp),
                    /*
                hint = "ben@abc.com",
                style = MaterialTheme.typography.body1,
                 */
                    value = emailInput,
                    onValueChange = { newInput: TextFieldValue ->
                        emailInput = newInput
                    }
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, start = 30.dp, end = 30.dp),
                    /*
                hint = "ben@abc.com",
                style = MaterialTheme.typography.body1,
                 */
                    value = passwordInput,
                    onValueChange = { newInput: TextFieldValue ->
                        passwordInput = newInput
                    }
                )
 */

