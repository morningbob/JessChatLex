package com.bitpunchlab.android.jesschatlex.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bitpunchlab.android.jesschatlex.helpers.WhoSaid
import com.bitpunchlab.android.jesschatlex.ui.theme.JessChatLex
import com.bitpunchlab.android.jesschatlex.userAccount.MainViewModel

@Composable
fun MessagesRecordScreen(navController: NavHostController,
                         mainViewModel: MainViewModel
) {
    // a list to display all messages
    // from the oldest to the newest
    // scroll to the newest automatically
    // load records in user info model
    // watch the list here
    LaunchedEffect(Unit) {
        mainViewModel.getAllMessages()
    }

    val allMessages by mainViewModel.allMessages.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        Column(modifier = Modifier.fillMaxWidth(0.8f)) {
            LazyColumn(modifier = Modifier
                .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
                ) {
                item {
                    allMessages.forEach { message ->
                        val textColor = if (message.whoSaid == WhoSaid.Lex) { JessChatLex.contentColor }
                        else { JessChatLex.messageColorUser }
                        Text(
                            text = message.message,
                            color = textColor,
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier
                                .padding(10.dp)
                        )
                    }
                }
            }
        }
    }
}