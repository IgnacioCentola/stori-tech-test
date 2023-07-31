package com.nacho.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nacho.home.HomeRoute

const val homeBaseRoute = "home"
private const val USER_ID_ARGUMENT_NAME = "userId"
private const val USER_ID_ARGUMENT_ROUTE = "{$USER_ID_ARGUMENT_NAME}"
private const val HOME_FULL_ROUTE = "$homeBaseRoute/$USER_ID_ARGUMENT_ROUTE"

fun NavController.navigateToHomeScreen(userId: String) {
    this.navigate("$homeBaseRoute/{$userId}")
}

fun NavGraphBuilder.homeScreen() {
    composable(
        route = HOME_FULL_ROUTE,
        arguments = listOf(navArgument(USER_ID_ARGUMENT_NAME) { type = NavType.StringType })
    ) {
        val userId = it.arguments?.getString(USER_ID_ARGUMENT_NAME)
        HomeRoute(userId = userId.orEmpty())
    }
}
