package com.nacho.storitechtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nacho.auth.LoginScreen
import com.nacho.auth.RegisterScreen
import com.nacho.storitechtest.ui.theme.StoriTechTestTheme

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
    val coroutineScope = rememberCoroutineScope()

    Surface(
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        NavHost(navController, startDestination = "login") {
            composable("login") { LoginScreen(navController) }
            composable("register") { RegisterScreen(navController) }
        }
    }
}