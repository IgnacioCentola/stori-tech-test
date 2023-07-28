package com.nacho.auth.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nacho.auth.components.StoriButton
import com.nacho.auth.components.StoriTextField


@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit = {},
    onLogin: (email: String, password: String) -> Unit = { _, _ -> }
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        var email by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Login", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))

            StoriTextField(
                value = email,
                label = "Email",
                imeAction = ImeAction.Next,
                onNextAction = { },
                onValueChange = { email = it },
                isEmailField = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            StoriTextField(
                value = password,
                label = "Password",
                isPasswordField = true,
                onValueChange = { password = it },
                onDoneAction = { })

            Spacer(modifier = Modifier.height(32.dp))

            StoriButton(
                text = "Login",
                onClick = { onLogin(email, password) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            StoriButton(
                text = "Register",
                onClick = { onNavigateToRegister.invoke() },
                modifier = Modifier.fillMaxWidth(),
                isOutlined = true
            )
        }
    }
}

@Preview
@Composable
fun LoginPreview() {
    LoginScreen()
}