package com.nacho.storitechtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.nacho.auth.navigation.loginRoute
import com.nacho.auth.navigation.loginScreen
import com.nacho.auth.navigation.navigateToRegisterScreen
import com.nacho.auth.navigation.registerScreen
import com.nacho.auth.viewmodel.AuthViewModel
import com.nacho.home.navigation.homeScreen
import com.nacho.home.navigation.navigateToHomeScreen
import com.nacho.model.AuthUiState
import com.nacho.storitechtest.ui.theme.StoriTechTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StoriTechTestTheme {
                AppContent()
            }
        }
    }
}


@Composable
fun AppContent() {
    val navController = rememberNavController()
    val authViewModel = viewModel<AuthViewModel>()
    val state by authViewModel.uiState.collectAsState()
    NavHost(navController, startDestination = loginRoute) {
        loginScreen(
            onNavigateToRegister = { navController.navigateToRegisterScreen() },
            onLogin = { email, password ->
                authViewModel.loginUser(email, password)
                when (state) {
                    is AuthUiState.Error -> navController.navigateToHomeScreen()
                    AuthUiState.Loading -> navController.navigateToHomeScreen()
                    is AuthUiState.Success -> navController.navigateToHomeScreen()
                }
            })
        registerScreen(
            onNavigateToLogin = {
                navController.popBackStack()
            },
            onRegister = { user, password ->
                authViewModel.registerUser(user, password)
                when (state) {
                    is AuthUiState.Error -> navController.navigateToHomeScreen()
                    AuthUiState.Loading -> navController.navigateToHomeScreen()
                    is AuthUiState.Success -> navController.navigateToHomeScreen()
                }
            })
        homeScreen(state)
    }

}