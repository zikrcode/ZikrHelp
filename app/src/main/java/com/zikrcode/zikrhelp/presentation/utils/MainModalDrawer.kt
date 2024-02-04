package com.zikrcode.zikrhelp.presentation.utils

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.zikrcode.zikrhelp.R
import com.zikrcode.zikrhelp.presentation.utils.navigation.MainNavigationActions
import com.zikrcode.zikrhelp.presentation.utils.navigation.Screen
import com.zikrcode.zikrhelp.ui.theme.ZikrHelpTheme
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