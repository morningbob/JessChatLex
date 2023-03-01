package com.bitpunchlab.android.jesschatlex.userAccount

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.bitpunchlab.android.jesschatlex.base.TitleText
import com.bitpunchlab.android.jesschatlex.ui.theme.JessChatLex

@Composable
fun ProfileScreen(navController: NavHostController, mainViewModel: MainViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(JessChatLex.brownBackground),

                ) {
                TitleText(title = "Profile", paddingTop = 60, paddingBottom = 60)
            }
            Text(text = "Name")
            Text(text = "")
            Text(text = "Email")
            Text(text = "")
            Text(text = "Password")
            Button(onClick = {
                mainViewModel.retrieveUserName()
            }) {
                Text(text = "Change Password")
            }

            // show name, email and change password button
        }
    }
}