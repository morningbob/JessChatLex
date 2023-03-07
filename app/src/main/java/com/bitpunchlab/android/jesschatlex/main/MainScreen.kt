package com.bitpunchlab.android.jesschatlex.main

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bitpunchlab.android.jesschatlex.Login
import com.bitpunchlab.android.jesschatlex.R
import com.bitpunchlab.android.jesschatlex.Records
import com.bitpunchlab.android.jesschatlex.awsClient.AmazonLexClient
import com.bitpunchlab.android.jesschatlex.awsClient.CognitoClient
import com.bitpunchlab.android.jesschatlex.base.CustomCircularProgressBar
import com.bitpunchlab.android.jesschatlex.base.SendIcon
import com.bitpunchlab.android.jesschatlex.helpers.ColorMode
import com.bitpunchlab.android.jesschatlex.helpers.Element
import com.bitpunchlab.android.jesschatlex.helpers.WhoSaid
import com.bitpunchlab.android.jesschatlex.models.Message
import com.bitpunchlab.android.jesschatlex.ui.theme.JessChatLex
import com.bitpunchlab.android.jesschatlex.userAccount.MainViewModel
import com.bitpunchlab.android.jesschatlex.userAccount.MainViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController,
    mainViewModel: MainViewModel,) {
    //userInfoViewModel: UserInfoViewModel = viewModel(LocalContext.current as ComponentActivity)) {

    val loadingAlpha by mainViewModel.loadingAlpha.collectAsState()

    val loginState by mainViewModel.isLoggedIn.collectAsState()
    var input by remember { mutableStateOf("") }

    var shouldNavigateRecords by remember { mutableStateOf(false) }

    val innerPadding = 70.dp

    LaunchedEffect(key1 = loginState) {
        if (!loginState) {
            navController.navigate(Login.route)
        }
    }

    LaunchedEffect(key1 = shouldNavigateRecords) {
        if (shouldNavigateRecords) {
            navController.navigate(Records.route)
        }
    }
    val lightMode = !isSystemInDarkTheme()
    fun chooseMode() : ColorMode {
        if (lightMode) {
            return ColorMode.LIGHT
        }
        return ColorMode.DARK
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),

            //.verticalScroll(rememberScrollState()),
        //color = JessChatLex.lightBlueBackground,
    ) {
        val themeMode = chooseMode()

        Scaffold(
            bottomBar = { BottomNavigationBar(navController
            ) },

        ) {
            //innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(JessChatLex.getColor(themeMode, Element.BACKGROUND)),//,//JessChatLex.lightBlueBackground),
                    //.navigationBarsPadding(),
                //.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,


            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight(0.75f)
                        .fillMaxWidth()
                        .padding(30.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    item {
                        mainViewModel.currentMessageList.forEach { message ->
                            val textColor = if (message.whoSaid == WhoSaid.Lex) {
                                JessChatLex.getColor(themeMode, Element.BOT_MESSAGE)
                            } else {
                                JessChatLex.getColor(themeMode, Element.USER_MESSAGE)
                                //JessChatLex.messageColorUser
                            }
                            Text(
                                text = message.message,
                                modifier = Modifier.padding(8.dp),
                                color = textColor,
                                fontSize = 20.sp
                            )
                        }
                    }
                }
                // this is the color of the cursor handle
                val customTextSelectionColors = TextSelectionColors(
                    handleColor = JessChatLex.getColor(themeMode, Element.FIELD_BORDER),
                    backgroundColor = JessChatLex.getColor(themeMode, Element.FIELD_BORDER),
                )

                CompositionLocalProvider(
                    LocalTextSelectionColors provides customTextSelectionColors,
                ) {

                    OutlinedTextField(
                        value = input,
                        onValueChange = { newInput: String ->
                            input = newInput
                        },
                        trailingIcon = {
                            if (input != "") {
                                SendIcon(color = JessChatLex.getColor(themeMode, Element.SEND_ICON)) {
                                    mainViewModel.sendMessage(input)
                                    input = ""
                                }
                            }
                        },
                        textStyle = LocalTextStyle.current.copy(color = JessChatLex.getColor(themeMode, Element.FIELD_BORDER)),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            //focusedLabelColor = JessChatLex.getColor(themeMode, Element.FIELD_BORDER),
                            //unfocusedLabelColor = JessChatLex.getColor(themeMode, Element.FIELD_BORDER),
                            focusedBorderColor = JessChatLex.getColor(
                                themeMode,
                                Element.FIELD_BORDER
                            ),//JessChatLex.blueBackground,
                            unfocusedBorderColor = JessChatLex.getColor(themeMode, Element.FIELD_BORDER),
                            placeholderColor = JessChatLex.getColor(themeMode, Element.FIELD_BORDER),
                            cursorColor = JessChatLex.getColor(themeMode, Element.FIELD_BORDER)
                        ),//JessChatLex.blueBackground),
                        //textStyle = LocalTextStyle.current.copy(color = textColor),
                        shape = RoundedCornerShape(12.dp),
                        label = { Text(
                            text = "Type your message",
                            color = JessChatLex.getColor(themeMode, Element.FIELD_BORDER),
                        ) }
                    )
                }

            }
            // progress bar
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(loadingAlpha),

                ) {
                CustomCircularProgressBar()
            }
        }
    }
}



