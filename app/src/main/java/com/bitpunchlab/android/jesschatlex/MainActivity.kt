package com.bitpunchlab.android.jesschatlex

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bitpunchlab.android.jesschatlex.ui.theme.JessChatLexTheme
import androidx.navigation.compose.rememberNavController
import com.amplifyframework.auth.AuthChannelEventName
import com.amplifyframework.core.InitializationStatus
import com.amplifyframework.hub.HubChannel
import com.amplifyframework.kotlin.core.Amplify
import com.bitpunchlab.android.jesschatlex.awsClient.AmazonLexClient
import com.bitpunchlab.android.jesschatlex.main.MainScreen
import com.bitpunchlab.android.jesschatlex.main.MessagesRecordScreen
import com.bitpunchlab.android.jesschatlex.userAccount.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

class MainActivity : ComponentActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //configureAmplify(applicationContext)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        //Amplify.configure({ object : AmplifyConfiguration() {
        //    aws_project_region: 'us-east-1'
        //}
        //}, applicationContext)

        AmazonLexClient.initializeLex(applicationContext)

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
                mainViewModel._isLoggedIn.value = true
                Log.i("main activity", "isLogged in ${mainViewModel._isLoggedIn.value}")
            }
        }
        setContent {
            JessChatLexTheme {
                // A surface container using the 'background' color from the theme
                JessNavigation(application)
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun JessNavigation(application: Application) {
    val navController = rememberNavController()
    val mainViewModel : MainViewModel = viewModel(factory = MainViewModelFactory(application))

    NavHost(navController = navController, startDestination = Login.route) {
        composable(Login.route) {
            LoginScreen(navController, mainViewModel)
        }
        composable(CreateAccount.route) {
            CreateAccountScreen(navController, mainViewModel)
        }
        composable(Main.route) {
            MainScreen(navController, mainViewModel)
        }
        composable(MessagesRecord.route) {
            MessagesRecordScreen(navController, mainViewModel)
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

