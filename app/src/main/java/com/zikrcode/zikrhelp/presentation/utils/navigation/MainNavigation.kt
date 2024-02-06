package com.zikrcode.zikrhelp.presentation.utils.navigation

import androidx.navigation.NavHostController

enum class Screen(val route: String) {
    ML_KIT_SCREEN("ml_kit"),
    OPEN_AI_SCREEN("open_ai")
}

class MainNavigationActions(private val navController: NavHostController) {

    fun navigateToMLKit() {
        navController.navigate(Screen.ML_KIT_SCREEN.route) {
            navController.graph.startDestinationRoute?.let {
                popUpTo(it) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    fun navigateToOpenAI() {
        navController.navigate(Screen.OPEN_AI_SCREEN.route) {
            navController.graph.startDestinationRoute?.let {
                popUpTo(it) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }
}
