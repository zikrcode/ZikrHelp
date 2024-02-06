package com.zikrcode.zikrhelp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.zikrcode.zikrhelp.presentation.utils.navigation.MainNavGraph
import com.zikrcode.zikrhelp.ui.theme.ZikrHelpTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            ZikrHelpTheme {
                MainActivityContent()
            }
        }
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    device = Devices.PHONE
)
@Composable
fun MainActivityContentPreview() {
    ZikrHelpTheme {
        MainActivityContent()
    }
}

@Composable
fun MainActivityContent() {
    MainNavGraph()
}