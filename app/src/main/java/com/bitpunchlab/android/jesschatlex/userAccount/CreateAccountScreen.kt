package com.bitpunchlab.android.jesschatlex.userAccount

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.bitpunchlab.android.jesschatlex.R
import com.bitpunchlab.android.jesschatlex.base.*

@Composable
fun CreateAccountScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var nameInput by remember { mutableStateOf(TextFieldValue("")) }
            var emailInput by remember { mutableStateOf(TextFieldValue("")) }
            var passwordInput by remember { mutableStateOf(TextFieldValue("")) }
            var confirmPasswordInput by remember { mutableStateOf(TextFieldValue("")) }
            var onSendClicked = { Log.i("onSendClicked", "received")
                Unit
            }

            HeaderImage(resource = R.mipmap.adduser, description = "Create Account logo",
                paddingTop = 0, paddingBottom = 0)
            TitleText(title = "Create Account", paddingTop = 30, paddingBottom = 30)
            DescriptionTitleText(title = "Name", paddingTop = 10, paddingBottom = 0)
            nameInput = UserInputTextField(hint = "Ben", hide = false)
            DescriptionTitleText(title = "Email", paddingTop = 10, paddingBottom = 0)
            emailInput = UserInputTextField(hint = "ben@abc.com", hide = false)
            DescriptionTitleText(title = "Password", paddingTop = 10, paddingBottom = 0)
            passwordInput = UserInputTextField(hint = "meqr06px", hide = true)
            DescriptionTitleText(title = "Confirm Password", paddingTop = 10, paddingBottom = 0)
            confirmPasswordInput = UserInputTextField(hint = "meqr06px", hide = true)
            //DescriptionTitleText(title = , paddingTop = , paddingBottom = )
            GeneralButton(title = "Send", onClick = { onSendClicked }, paddingTop = 20, paddingBottom = 20)
        }
    }
}