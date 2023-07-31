package com.nacho.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nacho.auth.screens.LoginRoute
import com.nacho.auth.screens.RegisterRoute

const val loginRoute = "login"
const val registerRoute = "register"

fun NavController.navigateToLoginScreen() {
    this.navigate(loginRoute)
}

fun NavGraphBuilder.loginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: (userId: String) -> Unit
) {
    composable(route = loginRoute) {
        LoginRoute(onNavigateToRegister = onNavigateToRegister, onLoginSuccess = onLoginSuccess)
    }
}

fun NavController.navigateToRegisterScreen() {
    this.navigate(registerRoute)
}

fun NavGraphBuilder.registerScreen(
    onNavigateToLogin: () -> Unit = {},
    onRegisterSuccess: (userId: String) -> Unit
) {
    composable(route = registerRoute) {
        RegisterRoute(onNavigateToLogin = onNavigateToLogin, onRegisterSuccess = onRegisterSuccess)
    }
}