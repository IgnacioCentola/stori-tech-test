package com.nacho.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nacho.auth.LoginScreen
import com.nacho.auth.RegisterScreen

const val loginRoute = "login"
const val registerRoute = "register"

fun NavController.navigateToLoginScreen() {
    this.navigate(loginRoute)
}

fun NavGraphBuilder.loginScreen(onNavigateToRegister: () -> Unit) {
    composable(route = loginRoute) {
        LoginScreen(onNavigateToRegister = onNavigateToRegister)
    }
}

fun NavController.navigateToRegisterScreen() {
    this.navigate(registerRoute)
}

fun NavGraphBuilder.registerScreen(onNavigateToLogin: () -> Unit = {}) {
    composable(route = registerRoute) {
        RegisterScreen(onNavigateToLogin = onNavigateToLogin)
    }
}

object OnboardingScreens{
    const val Name = 0
    const val Email = 1
    const val Password = 2
    const val Final = 3
}
