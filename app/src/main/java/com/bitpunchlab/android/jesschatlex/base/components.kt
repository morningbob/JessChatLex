package com.bitpunchlab.android.jesschatlex.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bitpunchlab.android.jesschatlex.R

@Composable
fun UserInputTextField(hint: String, hide: Boolean) : TextFieldValue {
    var inputText by remember { mutableStateOf(TextFieldValue("")) }

    val keyboardType = if (hide) { KeyboardType.Password} else { KeyboardType.Text }

    TextField(
        value = inputText,
        onValueChange = { newText: TextFieldValue ->
            inputText = newText
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, start = 30.dp, end = 30.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )

    return inputText
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
    Button(
        onClick = { onClick.invoke() },
        modifier = Modifier
            .padding(top = paddingTop.dp, start = 30.dp, end = paddingBottom.dp)
            .fillMaxWidth(),

        ) {
        Text(text = title)
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