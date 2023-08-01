package com.nacho.storitechtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.nacho.auth.navigation.loginRoute
import com.nacho.auth.navigation.loginScreen
import com.nacho.auth.navigation.navigateToRegisterScreen
import com.nacho.auth.navigation.registerScreen
import com.nacho.home.navigation.homeScreen
import com.nacho.home.navigation.navigateToHomeScreen
import com.nacho.storitechtest.ui.theme.StoriTechTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            StoriTechTestTheme {
                val navController = rememberNavController()
                val context = LocalContext.current

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ) {
                    NavHost(navController = navController, startDestination = loginRoute) {
                        loginScreen(
                            onNavigateToRegister = { navController.navigateToRegisterScreen() },
                            onLoginSuccess = { userId ->
                                navController.navigateToHomeScreen(userId)
                            }
                        )
                        registerScreen(
                            onNavigateToLogin = {
                                navController.popBackStack()
                            },
                            onRegisterSuccess = { userId ->
                                navController.navigateToHomeScreen(userId)
                            }
                        )
                        homeScreen()
                    }
                }
            }
        }
    }
}

