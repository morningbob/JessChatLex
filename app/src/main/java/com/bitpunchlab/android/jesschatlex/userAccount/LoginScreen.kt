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
    //val shouldNavigateMain by loginViewModel.shouldNavigateMain.collectAsState()
    //val shouldNavigateSignUp by loginViewModel.shouldNavigateSignUp.collectAsState()
    val loadingAlpha by loginViewModel.loadingAlpha.collectAsState()
    val loginState by mainViewModel.isLoggedIn.collectAsState()

    val readyLogin by loginViewModel.readyLogin.collectAsState()
    //Log.i("Login Screen", "should navigate signup : $shouldNavigateSignUp")
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
        var onSendClicked = {
            loginViewModel.loginUser()
        }
        var onSignUpClicked = {
            //LaunchedEffect(Unit) {
                //loginViewModel.navigateSignUp()
            //}
            navController.navigate(CreateAccount.route)
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .alpha(loadingAlpha),

            ) {
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
                //TextField(value = loginViewModel.email, onValueChange = { loginViewModel.updateEmail(it) })
                UserInputTextField(title = "Email", content = emailState, hide = false, paddingTop = 10, paddingBottom = 0
                ) { loginViewModel.updateEmail(it) }
                ErrorText(error = emailErrorState)
                UserInputTextField(title = "Password", content = passwordState, hide = true, paddingTop = 10, paddingBottom = 0
                ) { loginViewModel.updatePassword(it) }
                ErrorText(error = passwordErrorState)
            }
            GeneralButton(title = "Send", onClick = onSendClicked, paddingTop = 10, paddingBottom = 0, shouldEnable = readyLogin)
            GeneralButton(title = "Sign Up", onClick = onSignUpClicked, paddingTop = 10, paddingBottom = 20, shouldEnable = true)
            Button(onClick = {
                Log.i("logged in is ", mainViewModel.isLoggedIn.value.toString())
            }) {
                Text("test")
            }
        }
    }
}
/*
@Composable
fun LoginScreen(navController: NavHostController,
                userInfoViewModel: UserInfoViewModel,
                loginViewModel: LoginViewModel = viewModel(),
    //userInfoViewModel: UserInfoViewModel = viewModel(LocalContext.current as ComponentActivity)
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
                if (CognitoClient.loginUser(emailState, passwordState)) {
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
*/



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

