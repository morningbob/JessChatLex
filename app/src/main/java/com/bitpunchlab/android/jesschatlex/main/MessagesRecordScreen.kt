package com.bitpunchlab.android.jesschatlex.main

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.bitpunchlab.android.jesschatlex.ui.theme.JessChatLex
import com.bitpunchlab.android.jesschatlex.userAccount.UserInfoViewModel
import com.bitpunchlab.android.jesschatlex.userAccount.UserInfoViewModelFactory

@Composable
fun MessagesRecordScreen(navController: NavHostController,
                         userInfoViewModel: UserInfoViewModel) {
    // a list to display all messages
    // from the oldest to the newest
    // scroll to the newest automatically
    // load records in user info model
    // watch the list here
    //val userInfoViewModel : UserInfoViewModel = viewModel(factory =
    //UserInfoViewModelFactory(LocalContext.current.applicationContext as Application))
    LaunchedEffect(Unit) {
        userInfoViewModel.getAllMessages()
    }

    val allMessages by userInfoViewModel.allMessages.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        Column(modifier = Modifier.fillMaxWidth(0.8f)) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    allMessages.forEach { message ->
                        Text(
                            text = message.message,
                            color = JessChatLex.contentColor,
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            }
        }
    }
}