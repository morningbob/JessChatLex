package com.bitpunchlab.android.jesschatlex.base

import androidx.compose.foundation.*
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.bitpunchlab.android.jesschatlex.R
import com.bitpunchlab.android.jesschatlex.helpers.WhoSaid
import com.bitpunchlab.android.jesschatlex.ui.theme.JessChatLex

@Composable
fun UserInputTextField(title: String, content: String, hide: Boolean,
                       modifier: Modifier, trailingIcon: (@Composable () -> Unit)? = null,
                       textColor: Color = JessChatLex.blueText,
                       textBorder: Color = JessChatLex.blueBackground,
                        call: (String) -> Unit)  {

    val keyboardType = if (hide) { KeyboardType.Password } else { KeyboardType.Text }

    OutlinedTextField(
        label = { Text(
            text = title,
            color = textColor
        ) },
        value = content,
        onValueChange = { newText ->
            call.invoke(newText)
        },

        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 50.dp, end = 50.dp)
            .then(modifier),

        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = textBorder,
            unfocusedBorderColor = textBorder,
            focusedLabelColor = textBorder,
            unfocusedLabelColor = textBorder,
            cursorColor = textBorder,
            backgroundColor = Color.White,
            textColor = textColor,

    ),
        shape = RoundedCornerShape(12.dp),
        //trailingIcon = trailingIcon
        //trailingIcon = trailingIconFunction
    )
}

@Composable
fun TitleText(title: String, paddingTop: Int, paddingBottom: Int) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = paddingTop.dp, bottom = paddingBottom.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h1,
        fontSize = 40.sp,
        color = Color.White
    )
}

@Composable
fun GeneralText(textString: String, textColor: Color = MaterialTheme.colors.primary,
                textAlign: TextAlign = TextAlign.Start,
                size: TextUnit = 18.sp, modifier: Modifier,
                onClick: (() -> Unit)? = null) {
    Text(
        text = textString,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 50.dp, end = 50.dp)
            .clickable(enabled = onClick != null) {
                onClick?.invoke()
            }
            .then(modifier),
        color = textColor,
        fontSize = size,
        style = MaterialTheme.typography.body1,
        textAlign = textAlign
    )
}

@Composable
fun ErrorText(error: String, modifier: Modifier) {
    Text(
        text = error,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 3.dp, start = 50.dp, end = 50.dp)
            .then(modifier),
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.body2
    )
}

@Composable
fun GeneralButton(title: String, onClick: () -> Unit, shouldEnable: Boolean, paddingTop: Int, paddingBottom: Int) {

    val buttonTextColor = if (shouldEnable) JessChatLex.blueText else Color.Gray
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
fun AppButton(title: String, onClick: () -> Unit, shouldEnable: Boolean,
              buttonColor: Color = JessChatLex.blueBackground,
              buttonBackground: Color = JessChatLex.lightBlueBackground,
              modifier: Modifier) {
    Button(
        onClick = { onClick.invoke() },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = buttonColor,
            disabledContentColor = Color.Gray,
            disabledBackgroundColor = Color.Gray
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp, start = 70.dp, end = 70.dp)
            .background(buttonBackground)
            .then(modifier),

        shape = RoundedCornerShape(15.dp),
        enabled = shouldEnable)
    {
        Text(
            text = title,
            fontSize = 22.sp,
            color = Color.White
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
fun CustomDialog(title: String, message: String, backgroundColor: Color = JessChatLex.dialogBlueBackground,
                 buttonColor: Color = JessChatLex.blueText, textColor: Color = JessChatLex.blueText,
                 fieldOne: String? = null, fieldTwo: String? = null, onDismiss: () -> Unit,
                 okOnClick: ((List<String>?) -> Unit)? = null, cancelOnClick: ((List<String>?) -> Unit)? = null) {

    var fieldOneValue by remember {
        mutableStateOf("")
    }

    var fieldTwoValue by remember {
        mutableStateOf("")
    }

    Dialog(onDismissRequest = { onDismiss.invoke() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = backgroundColor
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
                    style = MaterialTheme.typography.h3,
                    color = textColor
                )

                Text(
                    text = message,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.body1,
                    color = textColor
                )

                if (fieldOne != null) {
                    OutlinedTextField(
                        value = fieldOneValue,
                        onValueChange = { newValue ->
                            fieldOneValue = newValue
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = JessChatLex.blueBackground,
                            unfocusedBorderColor = MaterialTheme.colors.primary,
                            focusedLabelColor = JessChatLex.blueBackground,
                            unfocusedLabelColor = JessChatLex.blueBackground
                        ),
                        shape = RoundedCornerShape(12.dp),
                        label = { Text(text = fieldOne) },
                        modifier = Modifier.padding(top = 30.dp)
                        // placeholder = { Text(text = "Type your message") }
                    )
                }

                if (fieldTwo != null) {
                    OutlinedTextField(
                        value = fieldTwoValue,
                        onValueChange = { newValue ->
                            fieldTwoValue = newValue
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = JessChatLex.blueBackground,
                            unfocusedBorderColor = MaterialTheme.colors.primary,
                            focusedLabelColor = JessChatLex.blueBackground,
                            unfocusedLabelColor = JessChatLex.blueBackground
                        ),
                        shape = RoundedCornerShape(12.dp),
                        label = { Text(text = fieldTwo) },
                        modifier = Modifier.padding(top = 30.dp)
                        // placeholder = { Text(text = "Type your message") }
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp, start = 50.dp, end = 50.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (okOnClick != null) {
                        DialogButton(
                            title = "OK",
                            color = buttonColor,
                            onClick = okOnClick,
                            modifier = Modifier,
                            parameter = listOf(fieldOneValue, fieldTwoValue)
                        )
                    }

                    if (cancelOnClick != null) {
                        DialogButton(
                            title = "Cancel",
                            color = buttonColor,
                            onClick = cancelOnClick,
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DialogButton(title: String, color: Color = JessChatLex.blueBackground,
                 parameter: List<String>? = null,
                 onClick: (List<String>?) -> Unit, modifier: Modifier) {

    Button(
        onClick = { onClick.invoke(parameter) },
        //modifier = Modifier
        //    .fillMaxWidth(0.5f),
        //.background(JessChatLex.buttonTextColor),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = color,
        ),
        modifier = modifier
    ) {
        Text(
            text = title,
            fontSize = 17.sp,
            color = Color.White
        )
    }
}

@Composable
fun CustomCircularProgressBar() {
    CircularProgressIndicator(
        modifier = Modifier.size(80.dp),
        color = JessChatLex.blueBackground,
        strokeWidth = 10.dp)

}

@Composable
fun sendIcon(onClick: () -> Unit) {
    IconButton(
        onClick = { onClick.invoke() }
    ) {
        Icon(
            painter = painterResource(id = R.mipmap.send),
            contentDescription = "Send Message",
            Modifier.size(30.dp),
            tint = JessChatLex.messageColorUser
        )
    }
}
