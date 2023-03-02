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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bitpunchlab.android.jesschatlex.base.CustomDialog
import com.bitpunchlab.android.jesschatlex.base.TitleText
import com.bitpunchlab.android.jesschatlex.main.BottomNavigationBar
import com.bitpunchlab.android.jesschatlex.main.sendIcon
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

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Scaffold(
            bottomBar = { BottomNavigationBar(navController
            ) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
                Text(
                    text = "Name",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 50.dp, end = 50.dp, top = 50.dp, bottom = 10.dp),
                    color = JessChatLex.buttonTextColor
                    )
                Text(
                    text = userName,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    color = JessChatLex.titleColor
                )
                Text(
                    text = "Email",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 50.dp, end = 50.dp, bottom = 10.dp),
                    color = JessChatLex.buttonTextColor
                )
                Text(
                    text = userEmail,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    color = JessChatLex.titleColor
                )
                Text(
                    text = "Password",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 50.dp, end = 50.dp, bottom = 10.dp),
                    //JessChatLex.titleColor
                    color = JessChatLex.buttonTextColor
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
                        color = JessChatLex.buttonTextColor
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .padding(start = 40.dp, end = 40.dp)
                    ) {
                        OutlinedTextField(
                            value = currentPassword,
                            onValueChange = { newValue ->
                                profileViewModel.updateCurrentPassword(newValue)
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = JessChatLex.buttonTextColor,
                                unfocusedBorderColor = MaterialTheme.colors.primary,
                                placeholderColor = JessChatLex.buttonTextColor),
                            shape = RoundedCornerShape(12.dp),
                            placeholder = { Text(text = "current password") },
                            modifier = Modifier
                                .padding(bottom = 3.dp)
                        )
                        Text(
                            text = currentPassError,
                            color = Color.Red,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = newPassword,
                            onValueChange = { newValue ->
                                profileViewModel.updateNewPassword(newValue)
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = JessChatLex.buttonTextColor,
                                unfocusedBorderColor = MaterialTheme.colors.primary,
                                placeholderColor = JessChatLex.buttonTextColor),
                            shape = RoundedCornerShape(12.dp),
                            placeholder = { Text(text = "new password") },
                            modifier = Modifier
                                .padding(bottom = 3.dp)
                        )
                        Text(
                            text = newPassError,
                            color = Color.Red,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { newValue ->
                                profileViewModel.updateConfirmPassword(newValue)
                            },
                            trailingIcon = {
                                if (currentPassError == "" && newPassError == "" && confirmPassError == "") {
                                    sendIcon {
                                        //mainViewModel.sendMessage(confirmPassword)
                                        //input = ""
                                    }
                                }
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = JessChatLex.buttonTextColor,
                                unfocusedBorderColor = MaterialTheme.colors.primary,
                                placeholderColor = JessChatLex.buttonTextColor),
                            shape = RoundedCornerShape(12.dp),
                            placeholder = { Text(text = "confirm password") },
                            // pad more at the bottom, there is a bottom navigation bar
                            modifier = Modifier
                                .padding(bottom = 3.dp)
                        )
                        Text(
                            text = confirmPassError,
                            color = Color.Red,
                            modifier = Modifier
                                .padding(bottom = 100.dp)
                        )
                    }

                }

                // show name, email and change password button
            }
            if (changePassResult == 1) {
                CustomDialog(
                    title = "Updated Password",
                    message = "Your password is updated.",
                    onDismiss = { profileViewModel.updateChangePassResult(0) },
                    okOnClick = { profileViewModel.updateChangePassResult(0) })
            } else if (changePassResult == 2) {
                CustomDialog(
                    title = "Update Password Failure",
                    message = "We couldn't update your password.  Please make sure you have wifi and try again.  If the problem persists, please contact admin@jessbitcom.pro",
                    onDismiss = { profileViewModel.updateChangePassResult(0) },
                    okOnClick = { profileViewModel.updateChangePassResult(0) })
            } // 0 - no dialog
        } // scaffold
    }
}