package com.bitpunchlab.android.jesschatlex.userAccount

import android.graphics.fonts.FontStyle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bitpunchlab.android.jesschatlex.Login
import com.bitpunchlab.android.jesschatlex.base.CustomCircularProgressBar

@Composable
fun LogoutScreen(navController: NavHostController,
                 mainViewModel: MainViewModel) {

    val loginState by mainViewModel.isLoggedIn.collectAsState()
    var loadingAlpha by remember {
        mutableStateOf(1f)
    }

    mainViewModel.logoutUser()
    //loadingAlpha = 1f

    LaunchedEffect(key1 = loginState) {
        if (!loginState) {
            navController.navigate(Login.route)
            loadingAlpha = 0f
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .alpha(loadingAlpha),

            ) {
            CustomCircularProgressBar()
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Logging Out",
                fontSize = 20.sp
            )
        }

    }
}