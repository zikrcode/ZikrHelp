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
