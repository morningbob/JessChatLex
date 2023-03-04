package com.bitpunchlab.android.jesschatlex.userAccount

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bitpunchlab.android.jesschatlex.base.*
import com.bitpunchlab.android.jesschatlex.ui.theme.JessChatLex

@Composable
fun ForgotPasswordScreen(navController: NavHostController,
    forgotPassViewModel: ForgotPassViewModel = viewModel()) {

    val verificationCode by forgotPassViewModel.verificationCode.collectAsState()
    val email by forgotPassViewModel.email.collectAsState()
    val newPassword by forgotPassViewModel.newPassword.collectAsState()
    val confirmPass by forgotPassViewModel.confirmPass.collectAsState()
    val codeError by forgotPassViewModel.codeError.collectAsState()
    val emailError by forgotPassViewModel.emailError.collectAsState()
    val newPassError by forgotPassViewModel.newPassError.collectAsState()
    val confirmPassError by forgotPassViewModel.confirmPassError.collectAsState()
    val readyReset by forgotPassViewModel.readyReset.collectAsState()
    val readyRequest by forgotPassViewModel.readyRequest.collectAsState()
    val resetPasswordStatus by forgotPassViewModel.resetPasswordStatus.collectAsState()

    var chosenOption by remember {
        mutableStateOf(0)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = JessChatLex.lightBrownBackground) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(JessChatLex.brownBackground)

                ) {
                TitleText(title = "Forgot Password", paddingTop = 100, paddingBottom = 100)
            }
            Text(
                text = "You need a verification code in order to change your password.",
                modifier = Modifier
                    .padding(top = 30.dp, bottom = 8.dp, start = 50.dp, end = 50.dp),
                color = JessChatLex.brownText
            )
            if (chosenOption == 0) {
                AppButton(
                    title = "I have a verification code",
                    onClick = {
                        chosenOption = 1
                              },
                    shouldEnable = true,
                    buttonColor = JessChatLex.brownBackground,
                    buttonBackground = JessChatLex.lightBrownBackground,
                    modifier = Modifier
                        .padding(top = 10.dp)
                )

                AppButton(
                    title = "I don't have a verification code",
                    onClick = {
                        chosenOption = 2
                              },
                    shouldEnable = true,
                    buttonColor = JessChatLex.brownBackground,
                    buttonBackground = JessChatLex.lightBrownBackground,
                    modifier = Modifier
                        .padding(top = 10.dp)
                )
            }

            if (chosenOption == 1) {
                ResetPasswordWidget(navigateCode = { chosenOption = 2 }, email = email, emailError = emailError, code = verificationCode,
                    codeError = codeError, newPassword = newPassword, newPassError = newPassError, 
                    confirmPass = confirmPass, confirmPassError = confirmPassError, readyReset = readyReset,
                    forgotPassViewModel = forgotPassViewModel)
            } else if (chosenOption == 2) {
                RequestCodeWidget(navigateRequest = { chosenOption = 1 }, email = email,
                    emailError = emailError, readyRequest = readyRequest,
                    forgotPassViewModel =forgotPassViewModel)
            }

        } // column
        when (resetPasswordStatus) {
            // change password succeeded
            1 -> {
                ResetPasswordSucceededDialog(forgotPassViewModel = forgotPassViewModel)
            }
            // change password failed
            2 -> {
                ResetPasswordFailedDialog(forgotPassViewModel = forgotPassViewModel)
            }
            // request verification code succeeded
            3 -> {
                SendCodeSucceededDialog(forgotPassViewModel = forgotPassViewModel)
            }
            // request verification code failed
            4 -> {
                SendCodeFailedDialog(forgotPassViewModel = forgotPassViewModel)
            }
        }
    } // surface
}

@Composable
fun ResetPasswordWidget(navigateCode: () -> Unit, email: String, emailError: String, code: String, codeError: String,
                        newPassword: String, newPassError: String, confirmPass: String,
                        confirmPassError: String, readyReset: Boolean, forgotPassViewModel: ForgotPassViewModel) {
    Column() {
        UserInputTextField(
            title = "Email",
            content = email,
            textColor = JessChatLex.brownText,
            textBorder = JessChatLex.brownBackground,
            hide = false,
            modifier = Modifier
                .padding(top = 10.dp, start = 20.dp, end = 20.dp),
            call = { forgotPassViewModel.updateEmail(it) })
        ErrorText(
            error = emailError,
            modifier = Modifier
                .padding(top = 2.dp, start = 30.dp, end = 30.dp),
            )

        UserInputTextField(
            title = "Enter Verification Code",
            content = code,
            textColor = JessChatLex.brownText,
            textBorder = JessChatLex.brownBackground,
            hide = false,
            modifier = Modifier
                .padding(top = 10.dp, start = 20.dp, end = 20.dp),
            call = { forgotPassViewModel.updateVerificationCode(it) })
        ErrorText(
            error = codeError,
            modifier = Modifier
                .padding(top = 2.dp, start = 30.dp, end = 30.dp)
        )


        UserInputTextField(
            title = "Enter a new password",
            content = newPassword,
            textColor = JessChatLex.brownText,
            textBorder = JessChatLex.brownBackground,
            hide = false,
            modifier = Modifier
                .padding(top = 4.dp, start = 20.dp, end = 20.dp),
            call = { forgotPassViewModel.updateNewPassword(it) })

        ErrorText(
            error = newPassError,
            modifier = Modifier
                .padding(top = 2.dp, start = 30.dp, end = 30.dp),
        )


        UserInputTextField(
            title = "Confirm the password",
            content = confirmPass,
            textColor = JessChatLex.brownText,
            textBorder = JessChatLex.brownBackground,
            hide = false,
            modifier = Modifier
                .padding(top = 4.dp, start = 20.dp, end = 20.dp),
            call = { forgotPassViewModel.updateConfirmPassword(it) })

        ErrorText(
            error = confirmPassError,
            modifier = Modifier
                .padding(top = 2.dp, start = 30.dp, end = 30.dp),
        )

        AppButton(
            title = "Change Password",
            onClick = {
                forgotPassViewModel.verifyCodeAndChangePassword(
                    email,
                    newPassword,
                    code
                )
            },
            shouldEnable = readyReset,
            buttonColor = JessChatLex.brownBackground,
            buttonBackground = JessChatLex.lightBrownBackground,
            modifier = Modifier
                .padding(top = 5.dp))

        Text(
            text = "I don't have verification code.",
            fontSize = 18.sp,
            color = JessChatLex.greenText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 50.dp)
                .clickable(enabled = true) {
                    navigateCode.invoke()
                },
            textAlign = TextAlign.Center

        )
    }
    
}

@Composable
fun RequestCodeWidget(navigateRequest: () -> Unit, email: String, emailError: String, readyRequest: Boolean,
                      forgotPassViewModel: ForgotPassViewModel) {
    Column() {
        UserInputTextField(
            title = "Email",
            content = email,
            textColor = JessChatLex.brownText,
            textBorder = JessChatLex.brownBackground,
            hide = false,
            modifier = Modifier
                .padding(top = 10.dp, start = 20.dp, end = 20.dp),
            call = { forgotPassViewModel.updateEmail(it) })
        Text(
            text = emailError,
            modifier = Modifier
                .padding(top = 2.dp, start = 60.dp, end = 60.dp),
            color = Color.Red
        )
        AppButton(
            title = "Request Code",
            onClick = {
                forgotPassViewModel.requestCode(email)
            },
            shouldEnable = readyRequest,
            buttonColor = JessChatLex.brownBackground,
            buttonBackground = JessChatLex.lightBrownBackground,
            modifier = Modifier
                .padding(top = 5.dp)
        )

        Text(
            text = "I have verification code.",
            fontSize = 18.sp,
            color = JessChatLex.greenText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 50.dp)
                .clickable(enabled = true) {
                    navigateRequest.invoke()
                },
            textAlign = TextAlign.Center
        )
    }

}

@Composable
fun ResetPasswordSucceededDialog(forgotPassViewModel: ForgotPassViewModel) {
    CustomDialog(
        title = "Reset Password Succeeded",
        message = "Your password is reset successfully.",
        backgroundColor = JessChatLex.lightBrownBackground,
        buttonColor = JessChatLex.brownBackground,
        textColor = JessChatLex.brownText,
        onDismiss = { forgotPassViewModel.updateResetPasswordStatus(0) },
        okOnClick = { forgotPassViewModel.updateResetPasswordStatus(0) })
}

@Composable
fun ResetPasswordFailedDialog(forgotPassViewModel: ForgotPassViewModel) {
    CustomDialog(
        title = "Reset Password Failed",
        message = "We couldn't reset your password.  Please make sure you have wifi.  Other than that, the server might be down.  Please try again later.  If the problem persists, please contact admin@jessbitcom.pro",
        backgroundColor = JessChatLex.lightBrownBackground,
        buttonColor = JessChatLex.brownBackground,
        textColor = JessChatLex.brownText,
        onDismiss = { forgotPassViewModel.updateResetPasswordStatus(0)},
        okOnClick = { forgotPassViewModel.updateResetPasswordStatus(0) })
}

@Composable
fun SendCodeSucceededDialog(forgotPassViewModel: ForgotPassViewModel) {
    CustomDialog(
        title = "Verification Code Sent",
        message = "We sent the verification code to your email.",
        backgroundColor = JessChatLex.lightBrownBackground,
        buttonColor = JessChatLex.brownBackground,
        textColor = JessChatLex.brownText,
        onDismiss = { forgotPassViewModel.updateResetPasswordStatus(0) },
        okOnClick = { forgotPassViewModel.updateResetPasswordStatus(0) })
}

@Composable
fun SendCodeFailedDialog(forgotPassViewModel: ForgotPassViewModel) {
    CustomDialog(
        title = "Verification Code",
        message = "We couldn't send the verification code.  Please make sure you have wifi.  Other than that, the server might be down.  Please try again later.  If the problem persists, please contact admin@jessbitcom.pro",
        backgroundColor = JessChatLex.lightBrownBackground,
        buttonColor = JessChatLex.brownBackground,
        textColor = JessChatLex.brownText,
        onDismiss = { forgotPassViewModel.updateResetPasswordStatus(0) },
        okOnClick = { forgotPassViewModel.updateResetPasswordStatus(0) })
}

