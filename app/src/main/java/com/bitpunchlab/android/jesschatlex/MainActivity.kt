package com.bitpunchlab.android.jesschatlex

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bitpunchlab.android.jesschatlex.ui.theme.JessChatLexTheme
import com.bitpunchlab.android.jesschatlex.userAccount.LoginScreen
import androidx.navigation.compose.rememberNavController
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.services.lexrts.AmazonLexRuntime
import com.amazonaws.services.lexrts.AmazonLexRuntimeClient
import com.amplifyframework.auth.AuthChannelEventName
import com.bitpunchlab.android.jesschatlex.userAccount.CreateAccountScreen
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.InitializationStatus
import com.amplifyframework.hub.HubChannel
import com.amplifyframework.kotlin.core.Amplify
import com.bitpunchlab.android.jesschatlex.main.MainScreen
import com.bitpunchlab.android.jesschatlex.userAccount.UserInfoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

class MainActivity : ComponentActivity() {

    private lateinit var userInfoViewModel: UserInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //configureAmplify(applicationContext)
        userInfoViewModel = ViewModelProvider(this).get(UserInfoViewModel::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val authSession = Amplify.Auth.fetchAuthSession(
                //{ Log.i("AmplifyQuickstart", "Auth session = ") },
                //{ error -> Log.e("AmplifyQuickstart", "Failed to fetch auth session", error) }
            )
            Log.i("AmplifyQuickstart", "Auth session = $authSession")
            //authListening()

            // check the auth session, if the user is signed it here is necessary
            // because listening to auth state change, doesn't not detect the first state
            if (authSession.isSignedIn) {
                Log.i("main activity", "set sign in as true")
                userInfoViewModel._isLoggedIn.value = true
                Log.i("main activity", "isLogged in ${userInfoViewModel._isLoggedIn.value}")
            }
            //AmazonLexRuntime
            //AmazonLexRuntimeClient
            //AWSMobileClient.getInstance().initialize(applicationContext, Callback<UserStateDetails>{
            AWSMobileClient.getInstance().initialize(applicationContext, Callback<UserStateDetails> {
                
            })
            //}
        }
        setContent {
            JessChatLexTheme {
                // A surface container using the 'background' color from the theme
                JessNavigation()
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun JessNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Login.route) {
        composable(Login.route) {
            LoginScreen(navController)
        }
        composable(CreateAccount.route) {
            CreateAccountScreen(navController)
        }
        composable(Main.route) {
            MainScreen(navController)
        }
    }
}

private suspend fun authListening() : Boolean =

    suspendCancellableCoroutine<Boolean> { cancellableContinuation ->
        CoroutineScope(Dispatchers.IO).launch {
        Amplify.Hub.subscribe(HubChannel.AUTH).collect {
            when (it.name) {
                InitializationStatus.SUCCEEDED.toString() ->
                    Log.i("AuthQuickstart", "Auth successfully initialized")
                InitializationStatus.FAILED.toString() ->
                    Log.i("AuthQuickstart", "Auth failed to succeed")
                else -> when (AuthChannelEventName.valueOf(it.name)) {
                    AuthChannelEventName.SIGNED_IN -> {
                        Log.i("AuthQuickstart", "Auth just became signed in.")
                        //isSignedIn = true
                        cancellableContinuation.resume(true) {}
                    }
                    AuthChannelEventName.SIGNED_OUT -> {
                        Log.i("AuthQuickstart", "Auth just became signed out.")
                        cancellableContinuation.resume(false) {}
                    }
                    AuthChannelEventName.SESSION_EXPIRED -> {
                        Log.i("AuthQuickstart", "Auth session just expired.")
                        cancellableContinuation.resume(false) {}
                    }
                    AuthChannelEventName.USER_DELETED -> {
                        Log.i("AuthQuickstart", "User has been deleted.")
                        cancellableContinuation.resume(false) {}
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JessChatLexTheme {
        Greeting("Android")
    }
}

