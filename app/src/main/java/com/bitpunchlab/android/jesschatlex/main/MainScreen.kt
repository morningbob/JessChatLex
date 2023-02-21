package com.bitpunchlab.android.jesschatlex.main

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.amplifyframework.auth.AuthChannelEventName
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.core.InitializationStatus
import com.amplifyframework.hub.HubChannel
import com.amplifyframework.kotlin.core.Amplify
import com.bitpunchlab.android.jesschatlex.base.GeneralButton
import com.bitpunchlab.android.jesschatlex.userAccount.UserInfoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MainScreen(navController: NavHostController,
    userInfoViewModel: UserInfoViewModel = viewModel()) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        GeneralButton(
            title = "Logout",
            onClick = { CoroutineScope(Dispatchers.IO).launch { logoutUser() } },
            shouldEnable = true,
            paddingTop = 0,
            paddingBottom = 0
        )
    }
}



private suspend fun logoutUser() {
    val signOutResult = Amplify.Auth.signOut()

    when(signOutResult) {
        is AWSCognitoAuthSignOutResult.CompleteSignOut -> {
            // Sign Out completed fully and without errors.
            Log.i("AuthQuickStart", "Signed out successfully")
        }
        is AWSCognitoAuthSignOutResult.PartialSignOut -> {
            // Sign Out completed with some errors. User is signed out of the device.
            signOutResult.hostedUIError?.let {
                Log.e("AuthQuickStart", "HostedUI Error", it.exception)
                // Optional: Re-launch it.url in a Custom tab to clear Cognito web session.

            }
            signOutResult.globalSignOutError?.let {
                Log.e("AuthQuickStart", "GlobalSignOut Error", it.exception)
                // Optional: Use escape hatch to retry revocation of it.accessToken.
            }
            signOutResult.revokeTokenError?.let {
                Log.e("AuthQuickStart", "RevokeToken Error", it.exception)
                // Optional: Use escape hatch to retry revocation of it.refreshToken.
            }
        }
        is AWSCognitoAuthSignOutResult.FailedSignOut -> {
            // Sign Out failed with an exception, leaving the user signed in.
            Log.e("AuthQuickStart", "Sign out Failed", signOutResult.exception)
        }
    }
}