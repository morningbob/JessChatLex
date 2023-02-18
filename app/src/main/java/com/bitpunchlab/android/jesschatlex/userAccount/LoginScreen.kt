package com.bitpunchlab.android.jesschatlex.userAccount

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bitpunchlab.android.jesschatlex.R
import com.bitpunchlab.android.jesschatlex.base.*

@Composable
fun LoginScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        var emailInput by remember { mutableStateOf(TextFieldValue("")) }
        var passwordInput by remember { mutableStateOf(TextFieldValue("")) }
        var onSendClicked = { Log.i("onSendClicked", "received in main")
            Unit
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {


            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                HeaderImage(resource = R.mipmap.login, description = "Login Icon",
                paddingTop = 0, paddingBottom = 0)
                TitleText(title = "Login", paddingTop = 5, paddingBottom = 0)
            }
            Column(horizontalAlignment = Alignment.Start) {
                DescriptionTitleText(title = "Email", paddingTop = 10, paddingBottom = 0)
                emailInput = UserInputTextField(hint = "ben@abc.com", hide = false)
                DescriptionTitleText(title = "Password", paddingTop = 10, paddingBottom = 0)
                passwordInput = UserInputTextField(hint = "fjeob46l", hide = true)
            }
            GeneralButton(title = "Send", onClick = onSendClicked, paddingTop = 20, paddingBottom = 20)
        }
    }
}
/*
TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, start = 30.dp, end = 30.dp),
                    /*
                hint = "ben@abc.com",
                style = MaterialTheme.typography.body1,
                 */
                    value = emailInput,
                    onValueChange = { newInput: TextFieldValue ->
                        emailInput = newInput
                    }
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, start = 30.dp, end = 30.dp),
                    /*
                hint = "ben@abc.com",
                style = MaterialTheme.typography.body1,
                 */
                    value = passwordInput,
                    onValueChange = { newInput: TextFieldValue ->
                        passwordInput = newInput
                    }
                )
 */

