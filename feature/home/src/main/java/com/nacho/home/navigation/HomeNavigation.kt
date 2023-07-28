package com.nacho.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nacho.home.HomeScreen
import com.nacho.model.AuthUiState

const val homeRoute = "home"

fun NavController.navigateToHomeScreen() {
    this.navigate(homeRoute)
}

fun NavGraphBuilder.homeScreen(uiState: AuthUiState) {
    composable(route = homeRoute) {
        HomeScreen(uiState)
    }
}
