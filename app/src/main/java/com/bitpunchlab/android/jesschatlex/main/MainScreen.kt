package com.bitpunchlab.android.jesschatlex.main

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.amplifyframework.auth.AuthChannelEventName
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.core.InitializationStatus
import com.amplifyframework.hub.HubChannel
import com.amplifyframework.kotlin.core.Amplify
import com.bitpunchlab.android.jesschatlex.Login
import com.bitpunchlab.android.jesschatlex.MessagesRecord
import com.bitpunchlab.android.jesschatlex.awsClient.AmazonLexClient
import com.bitpunchlab.android.jesschatlex.awsClient.CognitoClient
import com.bitpunchlab.android.jesschatlex.base.CustomCircularProgressBar
import com.bitpunchlab.android.jesschatlex.base.GeneralButton
import com.bitpunchlab.android.jesschatlex.helpers.WhoSaid
import com.bitpunchlab.android.jesschatlex.models.Message
import com.bitpunchlab.android.jesschatlex.ui.theme.JessChatLex
import com.bitpunchlab.android.jesschatlex.userAccount.UserInfoViewModel
import com.bitpunchlab.android.jesschatlex.userAccount.UserInfoViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun MainScreen(navController: NavHostController,
    userInfoViewModel: UserInfoViewModel,) {
    //userInfoViewModel: UserInfoViewModel = viewModel(LocalContext.current as ComponentActivity)) {
    //var userInfoViewModel : UserInfoViewModel? = null
    //LaunchedEffect(Unit) {
      //  userInfoViewModel =
      //      viewModel(factory = UserInfoViewModelFactory(LocalContext.current.applicationContext as Application))
    //}

    var loadProgressBar by remember { mutableStateOf(false) }
    var loadingAlpha = if (loadProgressBar) 1f else 0f

    val loginState by userInfoViewModel.isLoggedIn.collectAsState()
    var input by remember { mutableStateOf("") }

    val messageback by AmazonLexClient.messageState.collectAsState()

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = messageback) {
        if (messageback != "") {
            // we keep that current message list because we don't want to
            // display all messages from the database
            val message = Message(
                UUID.randomUUID().toString(),
                WhoSaid.Lex,
                messageback
            )
            userInfoViewModel.currentMessageList.add(
                message
            )
            // here we save it in local database
            userInfoViewModel.insertMessage(scope, message)

            // whenever there is a messageback triggered, the view is recreated (part of it)
            // we hide the box
            loadProgressBar = false
        }
    }

    var shouldNavigateRecords by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = loginState) {
        if (!loginState) {
            navController.navigate(Login.route)
        }
    }

    LaunchedEffect(key1 = shouldNavigateRecords) {
        if (shouldNavigateRecords) {
            navController.navigate(MessagesRecord.route)
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
            //.verticalScroll(rememberScrollState()),
        color = MaterialTheme.colors.background,

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
                //.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight(0.7f)
                    .padding(30.dp),
                //verticalArrangement = Arrangement.Center,
                //horizontalAlignment = Alignment.Start
            ) {
                item {

                    userInfoViewModel.currentMessageList.forEach { message ->
                        val textColor = if (message.whoSaid == WhoSaid.Lex) { JessChatLex.contentColor }
                            else { JessChatLex.messageColorUser }
                        Text(
                            text = message.message,
                            modifier = Modifier.padding(10.dp),
                            color = textColor
                        )
                    }
                }
            }

            OutlinedTextField(
                value = input, onValueChange = { newInput ->
                    input = newInput
                }
            )

            GeneralButton(
                title = "Send",
                onClick = {
                    loadProgressBar = true
                    AmazonLexClient.sendMessage(input)
                    val message = Message(UUID.randomUUID().toString(), WhoSaid.User, input)
                    userInfoViewModel.currentMessageList.add(message)
                    //CoroutineScope(Dispatchers.IO).launch {
                    userInfoViewModel.insertMessage(scope, message)
                    //}
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
                        //navController.navigate(MessagesRecord.route)
                },
                shouldEnable = true,
                paddingTop = 5,
                paddingBottom = 0
            )

            GeneralButton(
                title = "Logout",
                onClick = { CoroutineScope(Dispatchers.IO).launch { CognitoClient.logoutUser() } },
                shouldEnable = true,
                paddingTop = 10,
                paddingBottom = 20
            )
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


/*
            items(20) { index ->
                Text(
                    text = "item $index",
                    modifier = Modifier.height(50.dp),
                    //textAlign = TextAlign.Start
                )
            }

             */

