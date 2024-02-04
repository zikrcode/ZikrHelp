package com.zikrcode.zikrhelp.presentation.utils.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zikrcode.zikrhelp.presentation.ml_kit.MlKitScreen
import com.zikrcode.zikrhelp.presentation.open_ai.OpenAIScreen
import com.zikrcode.zikrhelp.presentation.utils.MainModalDrawer
import kotlinx.coroutines.launch

@Composable
fun MainNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination = Screen.OPEN_AI_SCREEN.route
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navActions = remember(navController) {
        MainNavigationActions(navController)
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: startDestination

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = Screen.OPEN_AI_SCREEN.route) {
            MainModalDrawer(drawerState, currentRoute, navActions) {
                OpenAIScreen {
                    coroutineScope.launch { drawerState.open() }
                }
            }
        }
        composable(route = Screen.ML_KIT_SCREEN.route) {
            MainModalDrawer(drawerState, currentRoute, navActions) {
                MlKitScreen{
                    coroutineScope.launch { drawerState.open() }
                }
            }
        }
    }
}