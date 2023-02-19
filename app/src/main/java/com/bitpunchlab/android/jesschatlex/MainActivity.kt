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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bitpunchlab.android.jesschatlex.ui.theme.JessChatLexTheme
import com.bitpunchlab.android.jesschatlex.userAccount.LoginScreen
import androidx.navigation.compose.rememberNavController
import com.bitpunchlab.android.jesschatlex.userAccount.CreateAccountScreen
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.kotlin.core.Amplify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //configureAmplify(applicationContext)
        CoroutineScope(Dispatchers.IO).launch {
            val authSession = Amplify.Auth.fetchAuthSession(
                //{ Log.i("AmplifyQuickstart", "Auth session = ") },
                //{ error -> Log.e("AmplifyQuickstart", "Failed to fetch auth session", error) }
            )
            Log.i("AmplifyQuickstart", "Auth session = $authSession")
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
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JessChatLexTheme {
        Greeting("Android")
    }
}

