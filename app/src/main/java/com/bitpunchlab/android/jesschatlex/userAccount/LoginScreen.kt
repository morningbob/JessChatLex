package com.bitpunchlab.android.jesschatlex.userAccount

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.cognito.options.AWSCognitoAuthSignInOptions
import com.amplifyframework.kotlin.core.Amplify
import com.bitpunchlab.android.jesschatlex.CreateAccount
import com.bitpunchlab.android.jesschatlex.Main
import com.bitpunchlab.android.jesschatlex.R
import com.bitpunchlab.android.jesschatlex.base.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine


@Composable
fun LoginScreen(navController: NavHostController,
                loginViewModel: LoginViewModel = viewModel(LocalContext.current as ComponentActivity),
                userInfoViewModel: UserInfoViewModel = viewModel(LocalContext.current as ComponentActivity)
) {

    val loginState by userInfoViewModel.isLoggedIn.collectAsState()
    Log.i("login screen", "isLoggedIn? $loginState")
    // LaunchedEffect is used to run code that won't trigger recomposition of the view
    LaunchedEffect(key1 = loginState) {
        if (loginState) {
            navController.navigate(Main.route)
        }
    }

    val emailState by loginViewModel.emailState.collectAsState()
    val passwordState by loginViewModel.passwordState.collectAsState()
    val emailErrorState by loginViewModel.emailErrorState.collectAsState()
    val passwordErrorState by loginViewModel.passwordErrorState.collectAsState()

    // to enable the send button or not, send to aws
    val shouldEnable = emailErrorState == "" && passwordErrorState == ""
    // to control to display the progress bar or not
    var loadProgressBar by remember { mutableStateOf(false) }
    val loadingAlpha = if (loadProgressBar) 1f else 0f


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        var onSendClicked = {
            loadProgressBar = true
            //Log.i("onSendClicked", "received in main")
            CoroutineScope(Dispatchers.IO).launch {
                if (loginUser(emailState, passwordState)) {
                    Log.i("login screen", "success passed to screen")
                    loadProgressBar = false
                    navController.navigate(Main.route)
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
            Log.i("email", emailState)
            Unit
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .alpha(loadingAlpha),

        ) {
            //CircularProgressIndicator()
            CustomCircularProgressBar()
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                HeaderImage(resource = R.mipmap.login, description = "Login Icon",
                paddingTop = 30, paddingBottom = 0)
                TitleText(title = "Login", paddingTop = 30, paddingBottom = 30)
            }
            Column(horizontalAlignment = Alignment.Start) {
                //DescriptionTitleText(title = "Email", paddingTop = 10, paddingBottom = 0)
                UserInputTextField(title = "Email", content = emailState, hide = false, paddingTop = 10, paddingBottom = 0
                ) { loginViewModel.updateEmail(it) }
                ErrorText(error = emailErrorState)
                //DescriptionTitleText(title = "Password", paddingTop = 10, paddingBottom = 0)
                UserInputTextField(title = "Password", content = passwordState, hide = true, paddingTop = 10, paddingBottom = 0
                ) { loginViewModel.updatePassword(it) }
                ErrorText(error = passwordErrorState)
            }
            GeneralButton(title = "Send", onClick = onSendClicked, paddingTop = 30, paddingBottom = 0, shouldEnable = shouldEnable)
            GeneralButton(title = "Sign Up", onClick = onSignUpClicked, paddingTop = 10, paddingBottom = 20, shouldEnable = true)
            GeneralButton(title = "test", onClick = printInputs, paddingTop = 10, paddingBottom = 10, shouldEnable = true)
            Text(text = "isLogged in $loginState")
            Button(onClick = {
                Log.i("logged in is ", userInfoViewModel.isLoggedIn.value.toString())
                //userInfoViewModel._isLoggedIn.value = true
            }) {
                Text("test")
            }
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

