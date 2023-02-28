package com.bitpunchlab.android.jesschatlex.userAccount

import android.graphics.fonts.FontStyle
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.cognito.options.AWSCognitoAuthSignInOptions
import com.amplifyframework.kotlin.core.Amplify
import com.bitpunchlab.android.jesschatlex.CreateAccount
import com.bitpunchlab.android.jesschatlex.Main
import com.bitpunchlab.android.jesschatlex.R
import com.bitpunchlab.android.jesschatlex.awsClient.CognitoClient
import com.bitpunchlab.android.jesschatlex.base.*
import com.bitpunchlab.android.jesschatlex.ui.theme.JessChatLex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.math.log


@Composable
fun LoginScreen(navController: NavHostController,
                mainViewModel: MainViewModel,
                loginViewModel: LoginViewModel = LoginViewModel(),
    //userInfoViewModel: UserInfoViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val emailState by loginViewModel.emailState.collectAsState()
    val passwordState by loginViewModel.passwordState.collectAsState()
    val emailErrorState by loginViewModel.emailErrorState.collectAsState()
    val passwordErrorState by loginViewModel.passwordErrorState.collectAsState()
    val loadingAlpha by loginViewModel.loadingAlpha.collectAsState()
    val loginState by mainViewModel.isLoggedIn.collectAsState()
    val showFailureDialog by loginViewModel.showFailureDialog.collectAsState()
    val showForgotDialog by loginViewModel.showForgotDialog.collectAsState()

    val readyLogin by loginViewModel.readyLogin.collectAsState()
    // LaunchedEffect is used to run code that won't trigger recomposition of the view
    LaunchedEffect(key1 = loginState) {
        if (loginState) {
            navController.navigate(Main.route)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        val onSendClicked = {
            loginViewModel.loginUser()
        }
        val onSignUpClicked = {
            //LaunchedEffect(Unit) {
                //loginViewModel.navigateSignUp()
            //}
            navController.navigate(CreateAccount.route)
        }
        //val onForgotPassClicked = {
        //    loginViewModel.updateForgotPassword(true)
        //}

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    //.padding(top = 0.dp, bottom = 30.dp, end = 80.dp)
                    .background(JessChatLex.buttonTextColor),

            ) {
                TitleText(title = "Login", paddingTop = 100, paddingBottom = 100)
            }
            /*
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                HeaderImage(resource = R.mipmap.login, description = "Login Icon",
                    paddingTop = 30, paddingBottom = 0)
                TitleText(title = "Login", paddingTop = 30, paddingBottom = 30)
            }
             */
            Column(horizontalAlignment = Alignment.Start) {
                //TextField(value = loginViewModel.email, onValueChange = { loginViewModel.updateEmail(it) })
                UserInputTextField(title = "Email", content = emailState, hide = false, paddingTop = 30, paddingBottom = 0
                ) { loginViewModel.updateEmail(it) }
                ErrorText(error = emailErrorState)
                UserInputTextField(title = "Password", content = passwordState, hide = true, paddingTop = 10, paddingBottom = 0
                ) { loginViewModel.updatePassword(it) }
                ErrorText(error = passwordErrorState)
            }
            AppButton(
                title = "Send",
                onClick = onSendClicked,
                shouldEnable = readyLogin,
                buttonColor = JessChatLex.buttonTextColor,
                modifier = Modifier
            )
            AppButton(
                title = "Sign Up",
                onClick = onSignUpClicked,
                shouldEnable = true,
                buttonColor = JessChatLex.buttonTextColor,
                modifier = Modifier
            )
            Text(
                text = "Forgot Password",
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, bottom = 30.dp)
                    .clickable(enabled = true) {
                        loginViewModel.updateShowForgotDialog(true)
                    },
                textAlign = TextAlign.Center,
                color = JessChatLex.buttonTextColor,

            )
        }
        if (showFailureDialog) {
            CustomDialog(
                title = "Login Failure",
                message = "Login failed.  Please make sure you have wifi.  Other than that, maybe the email or the password is not correct.  If the problem persists, please contact admin@jessbitcom.pro",
                onDismiss = { loginViewModel.updateShowDialog(false) },
                okOnClick = { loginViewModel.updateShowDialog(false) })
        }

        if (showForgotDialog) {
            CustomDialog(
                title = "Password Reset",
                message = "Please enter the email you use to register the app.  We'll send a password reset link to you.",
                field = "Email",
                onDismiss = { loginViewModel.updateShowForgotDialog(false) },
                okOnClick = { email ->
                    if (!email.isNullOrEmpty()) {
                        Log.i("login screen", "email $email")
                        loginViewModel.recoverUser(email)
                    }
                    loginViewModel.updateShowForgotDialog(false)
                },
                cancelOnClick = { loginViewModel.updateShowForgotDialog(false) })
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .alpha(loadingAlpha),

            ) {
            CustomCircularProgressBar()
        }
    }
}
