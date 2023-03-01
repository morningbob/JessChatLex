package com.bitpunchlab.android.jesschatlex.base

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.bitpunchlab.android.jesschatlex.R
import com.bitpunchlab.android.jesschatlex.helpers.WhoSaid
import com.bitpunchlab.android.jesschatlex.ui.theme.JessChatLex

@Composable
fun UserInputTextField(title: String, content: String, hide: Boolean,
                       paddingTop: Int, paddingBottom: Int,
                        call: (String) -> Unit)  {

    val keyboardType = if (hide) { KeyboardType.Password } else { KeyboardType.Text }

    OutlinedTextField(
        label = { Text(text = title) },
        value = content,
        onValueChange = { newText ->
            call.invoke(newText)
        },

        modifier = Modifier
            .fillMaxWidth()
            .padding(top = paddingTop.dp, bottom = paddingBottom.dp, start = 50.dp, end = 50.dp),

        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = JessChatLex.textFieldBorderColor,
            unfocusedBorderColor = JessChatLex.textFieldBorderColor,
            focusedLabelColor = MaterialTheme.colors.secondary,
            cursorColor = MaterialTheme.colors.primaryVariant,
            backgroundColor = Color.White
    ),
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
            .padding(top = 0.dp, bottom = 0.dp, start = 50.dp, end = 50.dp),
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
fun AppButton(title: String, onClick: () -> Unit, shouldEnable: Boolean, buttonColor: Color, modifier: Modifier) {
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
            .background(JessChatLex.background)
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
fun CustomDialog(title: String, message: String, fieldOne: String? = null, fieldTwo: String? = null, onDismiss: () -> Unit,
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
                    style = MaterialTheme.typography.h3
                )

                Text(
                    text = message,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.body1,
                    color = JessChatLex.dialogMessageColor
                )

                if (fieldOne != null) {
                    OutlinedTextField(
                        value = fieldOneValue,
                        onValueChange = { newValue ->
                            fieldOneValue = newValue
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = JessChatLex.buttonTextColor,
                            unfocusedBorderColor = MaterialTheme.colors.primary,
                            focusedLabelColor = JessChatLex.buttonTextColor,
                            unfocusedLabelColor = JessChatLex.buttonTextColor
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
                            focusedBorderColor = JessChatLex.buttonTextColor,
                            unfocusedBorderColor = MaterialTheme.colors.primary,
                            focusedLabelColor = JessChatLex.buttonTextColor,
                            unfocusedLabelColor = JessChatLex.buttonTextColor
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
                            onClick = okOnClick,
                            modifier = Modifier,
                            parameter = listOf(fieldOneValue, fieldTwoValue)
                        )
                    }

                    if (cancelOnClick != null) {
                        DialogButton(
                            title = "Cancel",
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
fun DialogButton(title: String, parameter: List<String>? = null, onClick: (List<String>?) -> Unit, modifier: Modifier) {

    Button(
        onClick = { onClick.invoke(parameter) },
        //modifier = Modifier
        //    .fillMaxWidth(0.5f),
        //.background(JessChatLex.buttonTextColor),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = JessChatLex.disabledGreen,
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
        color = JessChatLex.buttonTextColor,
        strokeWidth = 10.dp)

}
