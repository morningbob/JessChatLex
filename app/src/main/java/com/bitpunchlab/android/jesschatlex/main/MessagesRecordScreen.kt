package com.bitpunchlab.android.jesschatlex.main

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bitpunchlab.android.jesschatlex.helpers.WhoSaid
import com.bitpunchlab.android.jesschatlex.ui.theme.JessChatLex
import com.bitpunchlab.android.jesschatlex.userAccount.MainViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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
        color = JessChatLex.lightBlueBackground,
    ) {
        Scaffold(
            bottomBar = { BottomNavigationBar(navController
            ) }
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(JessChatLex.lightBlueBackground)
                        .padding(top = 30.dp, start = 30.dp, end = 30.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    item {
                        allMessages.forEach { message ->
                            val textColor = if (message.whoSaid == WhoSaid.Lex) {
                                JessChatLex.blueText
                            } else {
                                JessChatLex.messageColorUser
                            }
                            Text(
                                text = message.message,
                                color = textColor,
                                style = MaterialTheme.typography.body2,
                                modifier = Modifier
                                    .padding(top = 0.dp, bottom = 8.dp, start = 8.dp, end = 8.dp),
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}