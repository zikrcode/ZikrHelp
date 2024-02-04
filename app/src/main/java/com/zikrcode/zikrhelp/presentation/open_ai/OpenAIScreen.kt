package com.zikrcode.zikrhelp.presentation.open_ai

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.zikrcode.zikrhelp.R
import com.zikrcode.zikrhelp.ui.theme.ZikrHelpTheme

@Composable
fun OpenAIScreen(
    openDrawer: () -> Unit,
) {
    OpenAIContent(openDrawer)
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    device = Devices.PHONE
)
@Composable
fun OpenAIScreenPreview() {
    ZikrHelpTheme {
        OpenAIContent { }
    }
}

@Composable
private fun OpenAIContent(
    openDrawer: () -> Unit
) {
    Scaffold(
        topBar = {
            OpenAITopAppBar(openDrawer = openDrawer)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(text = stringResource(R.string.open_ai))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OpenAITopAppBar(
    openDrawer: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.open_ai))
        },
        navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(
                    painter = painterResource(R.drawable.ic_menu),
                    contentDescription = stringResource(R.string.open_drawer)
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}