package com.bitpunchlab.android.jesschatlex.userAccount

import android.annotation.SuppressLint
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bitpunchlab.android.jesschatlex.base.*
import com.bitpunchlab.android.jesschatlex.main.BottomNavigationBar
import com.bitpunchlab.android.jesschatlex.ui.theme.JessChatLex

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(navController: NavHostController, mainViewModel: MainViewModel,
    profileViewModel: ProfileViewModel = viewModel()) {

    val userName by mainViewModel.userName.collectAsState()
    val userEmail by mainViewModel.userEmail.collectAsState()
    val shouldChangePassword by profileViewModel.shouldChangePassword.collectAsState()
    val currentPassword by profileViewModel.currentPassword.collectAsState()
    val newPassword by profileViewModel.newPassword.collectAsState()
    val confirmPassword by profileViewModel.confirmPassword.collectAsState()
    val currentPassError by profileViewModel.currentPassError.collectAsState()
    val newPassError by profileViewModel.newPassError.collectAsState()
    val confirmPassError by profileViewModel.confirmPassError.collectAsState()
    val changePassResult by profileViewModel.changePassResult.collectAsState()
    val readyChange by profileViewModel.readyChange.collectAsState()
    val loadingAlpha by profileViewModel.loadingAlpha.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = JessChatLex.lightPurpleBackground
    ) {
        Scaffold(
            bottomBar = { BottomNavigationBar(navController
            ) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(JessChatLex.lightPurpleBackground)
                    .verticalScroll(rememberScrollState()),
                //verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .background(JessChatLex.purpleBackground)
                        .padding(top = 20.dp, bottom = 20.dp),

                    ) {
                    TitleText(title = "Profile", paddingTop = 100, paddingBottom = 100)
                }
                GeneralText(
                    textString = "Name",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 50.dp, bottom = 10.dp),
                    size = 18.sp,
                    textColor = Color.Black,
                )
                GeneralText(
                    textString = userName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    textColor = JessChatLex.purpleText,
                    textAlign = TextAlign.Center,
                    size = 18.sp
                )
                GeneralText(
                    textString = "Email",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                    textColor = Color.Black
                )
                GeneralText(
                    textString = userEmail,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp, bottom = 12.dp),
                    textAlign = TextAlign.Center,
                    textColor = JessChatLex.purpleText
                )
                GeneralText(
                    textString = "Password",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                    textColor = Color.Black
                )
                if (!shouldChangePassword) {
                    Text(
                        text = "Change Password",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(bottom = 30.dp)
                            .clickable(
                                enabled = true,
                                onClick = {
                                    profileViewModel.updateShouldChangePassword(true)
                                }),
                        color = JessChatLex.greenBackground
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                    ) {
                        UserInputTextField(
                            title = "Current Password",
                            content = currentPassword,
                            textColor = JessChatLex.purpleText,
                            textBorder = JessChatLex.purpleText,
                            hide = false,
                            modifier = Modifier.padding(bottom = 2.dp),
                            call = { profileViewModel.updateCurrentPassword(it)} )
                        ErrorText(
                            error = currentPassError,
                            modifier = Modifier
                                .padding(bottom = 4.dp, start = 20.dp, end = 20.dp),
                        )
                        UserInputTextField(
                            title = "New Password",
                            content = newPassword,
                            textColor = JessChatLex.purpleText,
                            textBorder = JessChatLex.purpleText,
                            hide = false,
                            modifier = Modifier.padding(bottom = 2.dp),
                            call = { profileViewModel.updateNewPassword(it)} )
                        ErrorText(
                            error = newPassError,
                            modifier = Modifier
                                .padding(bottom = 4.dp, start = 20.dp, end = 20.dp),
                        )

                        UserInputTextField(
                            title = "Confirm Password",
                            content = confirmPassword,
                            textColor = JessChatLex.purpleText,
                            textBorder = JessChatLex.purpleText,
                            hide = false,
                            modifier = Modifier.padding(bottom = 2.dp),
                            call = { profileViewModel.updateConfirmPassword(it) },
                        )
                        ErrorText(
                            error = confirmPassError,
                            modifier = Modifier
                                .padding(bottom = 4.dp, start = 20.dp, end = 20.dp),
                        )

                        AppButton(
                            title = "Send",
                            onClick = {
                                if (userEmail.isNotEmpty()) {
                                    profileViewModel.resetPassword(userEmail)
                                }
                              },
                            shouldEnable = readyChange,
                            buttonColor = JessChatLex.purpleBackground,
                            buttonBackground = JessChatLex.lightPurpleBackground,
                            modifier = Modifier
                                .padding(bottom = 100.dp)
                        )
                    }

                }

                // show name, email and change password button
            }
            if (changePassResult == 1) {
                ChangePasswordSuccessDialog(profileViewModel = profileViewModel)
            } else if (changePassResult == 2) {
                ChangePasswordFailureDialog(profileViewModel = profileViewModel)
            } // 0 - no dialog
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(loadingAlpha),

                ) {
                CustomCircularProgressBar()
            }
        } // scaffold
    }
}

@Composable
fun ChangePasswordSuccessDialog(profileViewModel: ProfileViewModel) {
    CustomDialog(
        title = "Updated Password",
        message = "Your password is updated.",
        backgroundColor = JessChatLex.lightPurpleBackground,
        buttonColor = JessChatLex.purpleBackground,
        textColor = JessChatLex.purpleText,
        onDismiss = { profileViewModel.updateChangePassResult(0) },
        okOnClick = { profileViewModel.updateChangePassResult(0) })
}

@Composable
fun ChangePasswordFailureDialog(profileViewModel: ProfileViewModel) {
    CustomDialog(
        title = "Update Password Failure",
        message = "We couldn't update your password.  Please make sure you have wifi and try again.  If the problem persists, please contact admin@jessbitcom.pro",
        backgroundColor = JessChatLex.lightPurpleBackground,
        buttonColor = JessChatLex.purpleBackground,
        textColor = JessChatLex.purpleText,
        onDismiss = { profileViewModel.updateChangePassResult(0) },
        okOnClick = { profileViewModel.updateChangePassResult(0) })
}