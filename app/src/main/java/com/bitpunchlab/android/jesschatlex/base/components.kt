package com.bitpunchlab.android.jesschatlex.base

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.bitpunchlab.android.jesschatlex.R
import com.bitpunchlab.android.jesschatlex.helpers.WhoSaid
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
fun ErrorText(error: String) {
    Text(
        text = error,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 0.dp, bottom = 0.dp, start = 30.dp, end = 30.dp),
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.body2
    )
}

@Composable
fun GeneralButton(title: String, onClick: () -> Unit, shouldEnable: Boolean, paddingTop: Int, paddingBottom: Int) {

    val buttonTextColor = if (shouldEnable) JessChatLex.buttonTextColor else Color.Gray
    val buttonBorderColor = if (shouldEnable) JessChatLex.buttonBorderColor else Color.Gray

    OutlinedButton(
        onClick = { onClick.invoke() },
        modifier = Modifier
            .padding(top = paddingTop.dp, bottom = paddingBottom.dp, start = 30.dp, end = 30.dp)
            .fillMaxWidth(0.5f),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = Color.Transparent,
            disabledContentColor = Color.Transparent,
            disabledBackgroundColor = Color.Transparent,
            ),

        border = BorderStroke(2.dp, buttonBorderColor),
        shape = RoundedCornerShape(12.dp),
        enabled = shouldEnable,

        ) {
        Text(
            text = title,
            color = buttonTextColor
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

@Composable
fun CustomDialog(title: String, message: String, setStartDialog: (Boolean) -> Unit,
                 okOnClick: () -> Unit, cancelOnClick: () -> Unit) {
    Dialog(onDismissRequest = { }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = JessChatLex.dialogBackgroundColor
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 30.dp, start = 30.dp, end = 30.dp)
            ) {

                Text(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h3)

                Text(
                    text = message,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.body1,
                    color = JessChatLex.dialogMessageColor
                )

                GeneralButton(
                    title = "OK",
                    onClick = okOnClick,
                    shouldEnable = true,
                    paddingTop = 0,
                    paddingBottom = 0
                )

                GeneralButton(
                    title = "Cancel",
                    onClick = { setStartDialog(false) },
                    shouldEnable = true,
                    paddingTop = 0,
                    paddingBottom = 0
                )

            }
        }
    }
}

@Composable
fun MessageText(whoSaid: WhoSaid, message: String) {

}