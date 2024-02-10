package com.zikrcode.zikrhelp.presentation.open_ai.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.zikrcode.zikrhelp.R
import com.zikrcode.zikrhelp.utils.Dimens
import com.zikrcode.zikrhelp.presentation.utils.composables.keyboardAsState
import com.zikrcode.zikrhelp.ui.theme.ZikrHelpTheme

@Composable
fun MessageContent(
    modifier: Modifier = Modifier,
    message: String,
    onMessageChange: (String) -> Unit,
    onSendMessage: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val isKeyboardOpen by keyboardAsState()
    if (!isKeyboardOpen) {
        focusManager.clearFocus(force = true)
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = message,
            onValueChange = onMessageChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = stringResource(R.string.message))
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus(force = true)
            }),
        )
        Spacer(Modifier.height(Dimens.SpacingDouble))
        Button(
            onClick = onSendMessage,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_send),
                contentDescription = null
            )
            Text(
                text = stringResource(R.string.send_message),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun MessageContentPreview() {
    ZikrHelpTheme {
        MessageContent(
            message = "",
            onMessageChange = { },
            onSendMessage = { }
        )
    }
}