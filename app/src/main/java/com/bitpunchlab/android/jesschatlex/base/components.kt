package com.bitpunchlab.android.jesschatlex.base

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bitpunchlab.android.jesschatlex.R
import com.bitpunchlab.android.jesschatlex.ui.theme.JessChatLex

@Composable
fun UserInputTextField(title: String, content: String, hide: Boolean,
                       paddingTop: Int, paddingBottom: Int,
                        call: (String) -> Unit)  {
    //var inputText by remember { mutableStateOf(TextFieldValue("")) }

    val keyboardType = if (hide) { KeyboardType.Password} else { KeyboardType.Text }

    OutlinedTextField(
        label = { Text(text = title) },
        value = content,
        onValueChange = { newText ->
            call.invoke(newText)
        },
        //onValueChange = { newText: TextFieldValue ->
        //    inputText = newText
        //},

        modifier = Modifier
            .fillMaxWidth()
            .padding(top = paddingTop.dp, bottom = paddingBottom.dp, start = 30.dp, end = 30.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = JessChatLex.textFieldBorderColor,
        unfocusedBorderColor = JessChatLex.textFieldBorderColor,
        focusedLabelColor = MaterialTheme.colors.secondary,
        cursorColor = MaterialTheme.colors.primaryVariant
    ),
    )

    //return inputText
}

@Composable
fun TitleText(title: String, paddingTop: Int, paddingBottom: Int) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = paddingTop.dp, bottom = paddingBottom.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h1
    )
}

@Composable
fun DescriptionTitleText(title: String, paddingTop: Int, paddingBottom: Int) {
    Text(
        text = title,
        modifier = Modifier
            .padding(top = paddingTop.dp, bottom = paddingBottom.dp, start = 30.dp),
        style = MaterialTheme.typography.h3
    )
}

@Composable
fun GeneralButton(title: String, onClick: () -> Unit, paddingTop: Int, paddingBottom: Int) {
        // OutlineTextField will be the content...
        OutlinedButton(
            onClick = { onClick.invoke() },
            modifier = Modifier
                .padding(top = paddingTop.dp, start = 30.dp, end = 30.dp)
                .fillMaxWidth(0.5f),
            colors = ButtonDefaults.buttonColors(JessChatLex.background),
            border = BorderStroke(2.dp, JessChatLex.buttonBorderColor),
            shape = RoundedCornerShape(12.dp)

            ) {
            Text(
                text = title,
                color = JessChatLex.buttonTextColor
            )
        }
}

@Composable
fun HeaderImage(resource: Int, description: String, paddingTop: Int, paddingBottom: Int) {
    Image(
        painter = painterResource(id = resource),
        modifier = Modifier
            .fillMaxWidth(0.4f)
            .padding(top = paddingTop.dp, bottom = paddingBottom.dp),
        contentDescription = description,

        )
}