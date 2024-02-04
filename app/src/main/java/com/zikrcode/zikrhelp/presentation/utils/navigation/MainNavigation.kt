package com.zikrcode.zikrhelp.presentation.utils.navigation

import androidx.navigation.NavHostController

enum class Screen(val route: String) {
    OPEN_AI_SCREEN("open_ai"),
    ML_KIT_SCREEN("ml_kit")
}

class MainNavigationActions(private val navController: NavHostController) {

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
}
