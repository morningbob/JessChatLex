package com.bitpunchlab.android.jesschatlex.userAccount

import android.graphics.fonts.FontStyle
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.bitpunchlab.android.jesschatlex.ForgotPassword
import com.bitpunchlab.android.jesschatlex.Main
import com.bitpunchlab.android.jesschatlex.R
import com.bitpunchlab.android.jesschatlex.awsClient.CognitoClient
import com.bitpunchlab.android.jesschatlex.base.*
import com.bitpunchlab.android.jesschatlex.helpers.ColorMode
import com.bitpunchlab.android.jesschatlex.helpers.Element
import com.bitpunchlab.android.jesschatlex.helpers.InputValidation
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
    val lightMode = !isSystemInDarkTheme()
    fun chooseMode() : ColorMode {
        if (lightMode) {
            return ColorMode.LIGHT_BLUE
        }
        return ColorMode.DARK_BLUE
    }

    val emailState by loginViewModel.emailState.collectAsState()
    val passwordState by loginViewModel.passwordState.collectAsState()
    val emailErrorState by loginViewModel.emailErrorState.collectAsState()
    val passwordErrorState by loginViewModel.passwordErrorState.collectAsState()
    val loadingAlpha by loginViewModel.loadingAlpha.collectAsState()
    val loginState by mainViewModel.isLoggedIn.collectAsState()
    val showFailureDialog by loginViewModel.showFailureDialog.collectAsState()
    val showConfirmEmailDialog by loginViewModel.showConfirmEmailDialog.collectAsState()
    val showConfirmEmailStatus by loginViewModel.showConfirmEmailStatus.collectAsState()
    
    val readyLogin by loginViewModel.readyLogin.collectAsState()
    // LaunchedEffect is used to run code that won't trigger recomposition of the view


    LaunchedEffect(key1 = loginState) {
        if (loginState) {
            navController.navigate(Main.route)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = JessChatLex.lightBlueBackground,

    ) {

        val mode = chooseMode()

        val onSendClicked = {
            loginViewModel.loginUser()
        }
        val onSignUpClicked = {
            navController.navigate(CreateAccount.route)
        }
        val onForgotPasswordClicked = {
            navController.navigate(ForgotPassword.route)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            //verticalArrangement = Arrangement.Center,
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    //.padding(top = 0.dp, bottom = 30.dp, end = 80.dp)
                    .background(JessChatLex.getColor(mode, Element.BANNER)),//JessChatLex.blueBackground),

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
                UserInputTextField(
                    title = "Email",
                    content = emailState,
                    textColor = JessChatLex.getColor(mode, Element.TEXT),//JessChatLex.blueText,
                    textBorder = JessChatLex.getColor(mode, Element.BANNER),
                    hide = false,
                    modifier = Modifier.padding(top = 30.dp),

                ) { loginViewModel.updateEmail(it) }
                ErrorText(error = emailErrorState, modifier = Modifier)
                UserInputTextField(
                    title = "Password",
                    content = passwordState,
                    textColor = JessChatLex.getColor(mode, Element.TEXT),//JessChatLex.blueText,
                    textBorder = JessChatLex.getColor(mode, Element.BANNER),//JessChatLex.blueBackground,
                    hide = true,
                    modifier = Modifier.padding(top = 10.dp),
                ) { loginViewModel.updatePassword(it) }
                ErrorText(error = passwordErrorState, modifier = Modifier)
            }
            AppButton(
                title = "Send",
                onClick = onSendClicked,
                shouldEnable = readyLogin,
                buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),//JessChatLex.blueBackground,
                buttonBackground = JessChatLex.getColor(mode, Element.BUTTON_BACKGROUND),//JessChatLex.lightBlueBackground,
                modifier = Modifier
            )
            AppButton(
                title = "Sign Up",
                onClick = onSignUpClicked,
                shouldEnable = true,
                buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),//JessChatLex.blueBackground,
                buttonBackground = JessChatLex.getColor(mode, Element.BUTTON_BACKGROUND),//JessChatLex.lightBlueBackground,
                modifier = Modifier
            )
            GeneralText(
                textString = "Forgot Password",
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 5.dp),
                textColor = JessChatLex.getColor(mode, Element.CLICKABLE),//JessChatLex.blueBackground,
                textAlign = TextAlign.Center,
                onClick = { onForgotPasswordClicked.invoke() }
            )
            GeneralText(
                textString = "Confirm Email",
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 5.dp),
                textColor = JessChatLex.getColor(mode, Element.CLICKABLE),//JessChatLex.blueBackground,
                textAlign = TextAlign.Center,
                onClick = { loginViewModel.updateShowConfirmEmailDialog(true) }
            )

        }
        if (showFailureDialog) {
            LoginFailureDialog(loginViewModel = loginViewModel, mode = mode)
        }

        if (showConfirmEmailDialog) {
            ConfirmEmailDialog(loginViewModel = loginViewModel, mode = mode)
        }

        if (showConfirmEmailStatus != 0) {
            ConfirmEmailResultDialog(confirmEmailStatus = showConfirmEmailStatus,
                loginViewModel = loginViewModel, mode = mode)
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



@Composable
fun LoginFailureDialog(loginViewModel: LoginViewModel, mode: ColorMode) {
    CustomDialog(
        title = "Login Failure",
        message = "Login failed.  Please make sure you have wifi.  Other than that, maybe the email or the password is not correct.  If the problem persists, please contact admin@jessbitcom.pro",
        backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),//JessChatLex.lightBlueBackground,
        buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),//JessChatLex.blueBackground,
        textColor = JessChatLex.getColor(mode, Element.TEXT),//JessChatLex.blueText,
        onDismiss = { loginViewModel.updateShowDialog(false) },
        okOnClick = { loginViewModel.updateShowDialog(false) })
}

@Composable
fun ConfirmEmailDialog(loginViewModel: LoginViewModel, mode: ColorMode) {
    CustomDialog(
        title = "Verification Code",
        message = "Please enter the verification code in the email.",
        backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),//JessChatLex.lightBlueBackground,
        buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),//JessChatLex.blueBackground,
        textColor = JessChatLex.getColor(mode, Element.TEXT),//JessChatLex.blueText,
        fieldOne = "Email",
        fieldTwo = "Confirmation Code",
        onDismiss = { loginViewModel.updateShowConfirmEmailDialog(false) },
        cancelOnClick = { loginViewModel.updateShowConfirmEmailDialog(false) },
        okOnClick = { inputList ->
            if (!inputList?.get(0).isNullOrEmpty() && !inputList?.get(1).isNullOrEmpty()) {
                Log.i("login screen", "email: ${inputList?.get(0)}")
                Log.i("login screen", "code: ${inputList?.get(1)}")
                loginViewModel.verifyConfirmCode(inputList!!.get(0), inputList!!.get(1))
            }
            loginViewModel.updateShowConfirmEmailDialog(false)
        }
    )
}

@Composable
fun ConfirmEmailResultDialog(confirmEmailStatus: Int, loginViewModel: LoginViewModel, mode: ColorMode) {
    if (confirmEmailStatus == 1) {
        CustomDialog(
            title = "Email Verification Success",
            message = "Your email and your account is verified.  Please login with the email.",
            backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),//JessChatLex.lightBlueBackground,
            buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),//JessChatLex.blueBackground,
            textColor = JessChatLex.getColor(mode, Element.TEXT),//JessChatLex.blueText,
            onDismiss = { loginViewModel.updateConfirmEmailStatus(0) },
            //cancelOnClick = { loginViewModel.updateConfirmEmailStatus(0) },
            okOnClick = { loginViewModel.updateConfirmEmailStatus(0) },
        )
    } else if (confirmEmailStatus == 2) {
        CustomDialog(
            title = "Email Verification Failure",
            message = "We couldn't verify your email.  Please make sure you have wifi, and the confirmation code is correct.",
            backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),//JessChatLex.lightBlueBackground,
            buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),//JessChatLex.blueBackground,
            textColor = JessChatLex.getColor(mode, Element.TEXT),//JessChatLex.blueText,
            onDismiss = { loginViewModel.updateConfirmEmailStatus(0) },
            //cancelOnClick = { loginViewModel.updateConfirmEmailStatus(0) },
            okOnClick = { loginViewModel.updateConfirmEmailStatus(0) },
        )
    }
}
