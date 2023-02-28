package com.bitpunchlab.android.jesschatlex.main

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.amplifyframework.auth.AuthChannelEventName
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.core.InitializationStatus
import com.amplifyframework.hub.HubChannel
import com.amplifyframework.kotlin.core.Amplify
import com.bitpunchlab.android.jesschatlex.Login
import com.bitpunchlab.android.jesschatlex.R
import com.bitpunchlab.android.jesschatlex.Records
import com.bitpunchlab.android.jesschatlex.awsClient.AmazonLexClient
import com.bitpunchlab.android.jesschatlex.awsClient.CognitoClient
import com.bitpunchlab.android.jesschatlex.base.CustomCircularProgressBar
import com.bitpunchlab.android.jesschatlex.base.GeneralButton
import com.bitpunchlab.android.jesschatlex.helpers.WhoSaid
import com.bitpunchlab.android.jesschatlex.models.Message
import com.bitpunchlab.android.jesschatlex.ui.theme.JessChatLex
import com.bitpunchlab.android.jesschatlex.userAccount.MainViewModel
import com.bitpunchlab.android.jesschatlex.userAccount.MainViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController,
    mainViewModel: MainViewModel,) {
    //userInfoViewModel: UserInfoViewModel = viewModel(LocalContext.current as ComponentActivity)) {

    val loadingAlpha by mainViewModel.loadingAlpha.collectAsState()

    val loginState by mainViewModel.isLoggedIn.collectAsState()
    var input by remember { mutableStateOf("") }

    var shouldNavigateRecords by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = loginState) {
        if (!loginState) {
            navController.navigate(Login.route)
        }
    }

    LaunchedEffect(key1 = shouldNavigateRecords) {
        if (shouldNavigateRecords) {
            navController.navigate(Records.route)
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
            //.verticalScroll(rememberScrollState()),
        color = MaterialTheme.colors.background,
    ) {
        Scaffold(
            bottomBar = { BottomNavigationBar(navController
                //title = { Text("Jess Chat") } ,
            ) }
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                //.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight(0.75f)
                        .fillMaxWidth()
                        .padding(30.dp),
                    horizontalAlignment = Alignment.Start
                    //verticalArrangement = Arrangement.Center,
                    //horizontalAlignment = Alignment.Start
                ) {
                    item {
                        mainViewModel.currentMessageList.forEach { message ->
                            val textColor = if (message.whoSaid == WhoSaid.Lex) {
                                JessChatLex.contentColor
                            } else {
                                JessChatLex.messageColorUser
                            }
                            Text(
                                text = message.message,
                                modifier = Modifier.padding(8.dp),
                                color = textColor,
                                fontSize = 20.sp
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = input,
                    onValueChange = { newInput : String ->
                        input = newInput
                    },
                    trailingIcon = {
                        if (input != "") {
                            sendIcon {
                                mainViewModel.sendMessage(input)
                                input = ""
                            }
                        }
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = JessChatLex.buttonTextColor,
                        unfocusedBorderColor = MaterialTheme.colors.primary,
                        placeholderColor = JessChatLex.buttonTextColor),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = { Text(text = "Type your message") }
                )
/*
                GeneralButton(
                    title = "Send",
                    onClick = {
                        mainViewModel.sendMessage(input)
                        input = ""
                    },
                    shouldEnable = true,
                    paddingTop = 5,
                    paddingBottom = 0
                )

                GeneralButton(
                    title = "Chat Record",
                    onClick = {
                        shouldNavigateRecords = true
                    },
                    shouldEnable = true,
                    paddingTop = 5,
                    paddingBottom = 0
                )

                GeneralButton(
                    title = "Logout",
                    onClick = { mainViewModel.logoutUser() },
                    shouldEnable = true,
                    paddingTop = 10,
                    paddingBottom = 20
                )

 */
            }
            // progress bar
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
}

@Composable
fun sendIcon(onClick: () -> Unit) {
    IconButton(
        onClick = { onClick.invoke() }
    ) {
        Icon(
            painter = painterResource(id = R.mipmap.send),
            contentDescription = "Send Message",
            Modifier.size(30.dp),
            tint = JessChatLex.messageColorUser
        )
    }
}

