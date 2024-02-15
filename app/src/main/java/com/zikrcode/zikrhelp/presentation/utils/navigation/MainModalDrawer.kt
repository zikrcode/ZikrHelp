/*
 * Copyright 2024 Zokirjon Mamadjonov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zikrcode.zikrhelp.presentation.utils.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zikrcode.zikrhelp.R
import com.zikrcode.zikrhelp.utils.Dimens
import com.zikrcode.zikrhelp.ui.theme.ZikrHelpTheme
import com.zikrcode.zikrhelp.utils.AppConstants.ZIKRCODE_URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainModalDrawer(
    drawerState: DrawerState,
    currentRoute: String,
    navigationActions: MainNavigationActions,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                DrawerSheetContent(
                    currentRoute = currentRoute,
                    navigateToOpenAI = { navigationActions.navigateToOpenAI() },
                    navigateToMLKit = { navigationActions.navigateToMLKit() },
                    closeDrawer = { coroutineScope.launch { drawerState.close() } }
                )
            }
        },
        drawerState = drawerState
    ) {
        content()
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    device = Devices.PHONE
)
@Composable
fun DrawerSheetContentPreview() {
    ZikrHelpTheme {
        DrawerSheetContent(
            currentRoute = "",
            navigateToOpenAI = { },
            navigateToMLKit = { },
            closeDrawer = { }
        )
    }
}

@Composable
private fun DrawerSheetContent(
    currentRoute: String,
    navigateToOpenAI: () -> Unit,
    navigateToMLKit: () -> Unit,
    closeDrawer: () -> Unit
) {
    ZikrHelpLogo(
        modifier = Modifier.padding(
            horizontal = Dimens.SpacingTriple + Dimens.SpacingHalf,
            vertical = Dimens.SpacingTriple
        )
    )
    NavigationDrawerItem(
        label = {
            Text(text = stringResource(R.string.ml_kit))
        },
        selected = currentRoute == Screen.ML_KIT_SCREEN.route,
        onClick = {
            navigateToMLKit()
            closeDrawer()
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_ml_kit),
                contentDescription = null,
                modifier = Modifier.size(Dimens.SpacingTriple),
            )
        }
    )
    NavigationDrawerItem(
        label = {
            Text(text = stringResource(R.string.open_ai))
        },
        selected = currentRoute == Screen.OPEN_AI_SCREEN.route,
        onClick = {
            navigateToOpenAI()
            closeDrawer()
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_open_ai),
                contentDescription = null,
                modifier = Modifier.size(Dimens.SpacingTriple),
            )
        }
    )
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        ZikrCodeLogo()
    }
}

@Composable
private fun ZikrHelpLogo(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Icon(
            painter = painterResource(R.drawable.ic_zikr_help_logo),
            contentDescription = null,
            modifier = Modifier.size(Dimens.SpacingTriple),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.width(Dimens.SpacingSingle))
        Text(text = stringResource(R.string.app_name))
    }
}

@Composable
private fun ZikrCodeLogo() {
    val context = LocalContext.current
    val chooserTitle = stringResource(R.string.open_url_with)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Divider()
        Spacer(Modifier.height(Dimens.SpacingDouble))
        Text(
            text = stringResource(R.string.developed_by),
            style = MaterialTheme.typography.titleMedium
        )
        Icon(
            painter = painterResource(R.drawable.zikrcode_logo),
            contentDescription = null,
            modifier = Modifier
                .width(150.dp)
                .clip(RoundedCornerShape(Dimens.CornerRadiusSingleHalf))
                .clickable {
                    val uri = Uri.parse(ZIKRCODE_URL)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    val chooser = Intent.createChooser(intent, chooserTitle)
                    context.startActivity(chooser)
                },
            tint = Color.Unspecified
        )
    }
}