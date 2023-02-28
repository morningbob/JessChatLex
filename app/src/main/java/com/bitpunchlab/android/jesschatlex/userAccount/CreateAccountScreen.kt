package com.bitpunchlab.android.jesschatlex.userAccount

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.kotlin.core.Amplify
import com.bitpunchlab.android.jesschatlex.Login
import com.bitpunchlab.android.jesschatlex.Main
import com.bitpunchlab.android.jesschatlex.R
import com.bitpunchlab.android.jesschatlex.awsClient.CognitoClient
import com.bitpunchlab.android.jesschatlex.base.*
import com.bitpunchlab.android.jesschatlex.ui.theme.JessChatLex
import kotlinx.coroutines.*

@Composable
fun CreateAccountScreen(navController: NavHostController,
    mainViewModel: MainViewModel,
    registerViewModel: RegisterViewModel = viewModel())
{
    val nameState by registerViewModel.nameState.collectAsState()
    val emailState by registerViewModel.emailState.collectAsState()
    val passwordState by registerViewModel.passwordState.collectAsState()
    val confirmPassState by registerViewModel.confirmPassState.collectAsState()
    val nameErrorState by registerViewModel.nameErrorState.collectAsState()
    val emailErrorState by registerViewModel.emailErrorState.collectAsState()
    val passwordErrorState by registerViewModel.passwordErrorState.collectAsState()
    val confirmPassErrorState by registerViewModel.confirmPassErrorState.collectAsState()
    val loadingAlpha by registerViewModel.loadingAlpha.collectAsState()
    val loginState by mainViewModel.isLoggedIn.collectAsState()
    val showFailureDialog by registerViewModel.showFailureDialog.collectAsState()
    val showSuccessDialog by registerViewModel.showSuccessDialog.collectAsState()
    val readyRegister by registerViewModel.readyRegister.collectAsState()
    val shouldRedirectLogin by registerViewModel.shouldRedirectLogin.collectAsState()

    // navigate to main page if the user successfully created the account
    // or when user navigate to this page by back button
    // but he is already logged in

    LaunchedEffect(key1 = loginState) {
        if (loginState) {
            navController.navigate(Main.route)
        }
    }

    LaunchedEffect(key1 = shouldRedirectLogin) {
        if (shouldRedirectLogin) {
            navController.navigate(Login.route)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                var onSendClicked = {
                    registerViewModel.registerUser()
                }
                var onLoginClicked = {
                    //registerViewModel.navigateLogin()
                    navController.navigate(Login.route)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        //.padding(top = 0.dp, bottom = 30.dp, end = 80.dp)
                        .background(JessChatLex.greenBackground),

                    ) {
                    TitleText(title = "Register", paddingTop = 60, paddingBottom = 60)
                }

                //HeaderImage(
                //    resource = R.mipmap.adduser, description = "Create Account logo",
                //    paddingTop = 30, paddingBottom = 0
                //)
                //TitleText(title = "Create Account", paddingTop = 30, paddingBottom = 30)
                Column(horizontalAlignment = Alignment.Start) {
                    UserInputTextField(title = "Name", content = nameState, hide = false, paddingTop = 30, paddingBottom = 0
                    ) { registerViewModel.updateName(it) }
                    ErrorText(error = nameErrorState)
                    UserInputTextField(title = "Email", content = emailState, hide = false, paddingTop = 10, paddingBottom = 0
                    ) { registerViewModel.updateEmail(it) }
                    ErrorText(error = emailErrorState)
                    UserInputTextField(title = "Password", content = passwordState, hide = true, paddingTop = 10, paddingBottom = 0
                    ) { registerViewModel.updatePassword(it) }
                    ErrorText(error = passwordErrorState)
                    UserInputTextField(title = "Confirm Password", content = confirmPassState, hide = true,
                        paddingTop = 10, paddingBottom = 0) {
                        registerViewModel.updateConfirmPassword(it)
                    }
                    ErrorText(error = confirmPassErrorState)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AppButton(
                        title = "Send",
                        onClick = onSendClicked,
                        shouldEnable = readyRegister,
                        buttonColor = JessChatLex.greenBackground,
                        modifier = Modifier
                    )
                    AppButton(
                        title = "Login",
                        onClick = onLoginClicked,
                        shouldEnable = true,
                        buttonColor = JessChatLex.greenBackground,
                        modifier = Modifier
                    )

                }
            }
        }
        if (showFailureDialog) {
            CustomDialog(
                title = "Registration Failure",
                message = "There is error registering your account.  Please make sure you have wifi, and the email is not registered before.  Other than that, the server may be in maintenance.  If the problem persists, please contact admin@jessbitcom.pro",
                onDismiss = { registerViewModel.updateShowDialog(false) },
                okOnClick = { registerViewModel.updateShowDialog(false) }
            )
        }
        if (showSuccessDialog) {
            CustomDialog(
                title = "Registration Success",
                message = "You are successfully registered.  Please login the app.",
                onDismiss = { registerViewModel.updateShowSuccessDialog(false) },
                okOnClick = { registerViewModel.updateShowSuccessDialog(false) }
            )
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




