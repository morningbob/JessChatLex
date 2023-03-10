package com.bitpunchlab.android.jesschatlex

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bitpunchlab.android.jesschatlex.ui.theme.JessChatLexTheme
import androidx.navigation.compose.rememberNavController
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.kotlin.core.Amplify
import com.bitpunchlab.android.jesschatlex.awsClient.AmazonLexClient
import com.bitpunchlab.android.jesschatlex.main.MainScreen
import com.bitpunchlab.android.jesschatlex.main.MessagesRecordScreen
import com.bitpunchlab.android.jesschatlex.userAccount.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        AmazonLexClient.initializeLex(applicationContext)
        //Amplify.Auth.rememberDevice()

        CoroutineScope(Dispatchers.IO).launch {
            val authSession = Amplify.Auth.fetchAuthSession(
                //{ Log.i("AmplifyQuickstart", "Auth session = ") },
                //{ error -> Log.e("AmplifyQuickstart", "Failed to fetch auth session", error) }
            )
            Log.i("Main activity", "Auth session = $authSession")
            // retrieve tokens
            val auth = authSession as AWSCognitoAuthSession
            Log.i("token: " , auth.userPoolTokensResult.value?.accessToken.toString())

            //authListening()

            // check the auth session, if the user is signed it here is necessary
            // because listening to auth state change, doesn't not detect the first state
            if (authSession.isSignedIn) {
                Log.i("main activity", "set sign in as true")
                mainViewModel._isLoggedIn.value = true
                Log.i("main activity", "isLogged in ${mainViewModel._isLoggedIn.value}")
            } else {
                Log.i("main activity", "is not signed in")
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
fun JessNavigation(application: Application) {
    val navController = rememberNavController()
    val mainViewModel : MainViewModel = viewModel(factory = MainViewModelFactory(application))

    //Box(modifier = Modifier.navigationBarsPadding()) {

        NavHost(navController = navController, startDestination = Login.route) {
            composable(Login.route) {
                LoginScreen(navController, mainViewModel)
            }
            composable(CreateAccount.route) {
                CreateAccountScreen(navController, mainViewModel)
            }
            composable(ForgotPassword.route) {
                ForgotPasswordScreen(navController)
            }
            composable(Main.route) {
                MainScreen(navController, mainViewModel)
            }
            composable(Records.route) {
                MessagesRecordScreen(navController, mainViewModel)
            }
            composable(Profile.route) {
                ProfileScreen(navController, mainViewModel)
            }
            composable(Logout.route) {
                LogoutScreen(navController, mainViewModel)
            }

        }
    //}
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JessChatLexTheme {

    }
}

