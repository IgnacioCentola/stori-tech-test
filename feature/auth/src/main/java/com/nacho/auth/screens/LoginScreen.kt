package com.nacho.auth.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nacho.auth.components.StoriButton
import com.nacho.auth.components.StoriTextField
import com.nacho.auth.viewmodel.AuthViewModel
import com.nacho.model.AuthUiState


@Composable
internal fun LoginRoute(
    onNavigateToRegister: () -> Unit = {},
    onLoginSuccess: (userId: String) -> Unit = { },
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by authViewModel.uiState.collectAsState(initial = AuthUiState.Default)
    LoginScreen(
        onLogin = { email, password ->
            authViewModel.loginUser(email, password)
        },
        onNavigateToRegister = onNavigateToRegister,
        uiState = uiState,
        onLoginSuccess = {
            onLoginSuccess(authViewModel.getCachedUserId())
        }
    )

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun LoginScreen(
    onNavigateToRegister: () -> Unit = {},
    onLogin: (email: String, password: String) -> Unit = { _, _ -> },
    onLoginSuccess: () -> Unit = {},
    uiState: AuthUiState = AuthUiState.Loading
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var showLoadingDialog by remember {
        mutableStateOf(true)
    }
    when (uiState) {
        is AuthUiState.Error -> ErrorState(
            msg = uiState.msg,
            showDialog = showLoadingDialog,
            onDismissRequest = {
                showLoadingDialog = !showLoadingDialog
            }
        )

        AuthUiState.Loading -> LoadingState(
            showDialog = showLoadingDialog,
            onDismissRequest = {
                showLoadingDialog = !showLoadingDialog
            }
        )

        is AuthUiState.Default -> DefaultState(
            onLogin = onLogin,
            onNavigateToRegister = onNavigateToRegister,
            onDoneKeyboardAction = {
                keyboardController?.hide()
            }
        )

        AuthUiState.Success -> {
            LoadingState(showDialog = true, isSuccess = true)
            onLoginSuccess.invoke()
        }
    }

}

@Composable
private fun DefaultState(
    onNavigateToRegister: () -> Unit = {},
    onLogin: (email: String, password: String) -> Unit = { _, _ -> },
    onNextKeyboardAction: () -> Unit = {},
    onDoneKeyboardAction: () -> Unit = {},
) {
    Log.d("PERON", "Login Default state")

    var email by rememberSaveable { mutableStateOf("test@test.com") }
    var password by rememberSaveable { mutableStateOf("123456") }

    var isEmailEmptyError by rememberSaveable { mutableStateOf(false) }
    var isPasswordEmptyError by rememberSaveable { mutableStateOf(false) }

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
            onNextAction = onNextKeyboardAction,
            onValueChange = {
                email = it
                isEmailEmptyError = email.isEmpty()
            },
            isEmailField = true,
            isEmptyError = isEmailEmptyError
        )

        Spacer(modifier = Modifier.height(16.dp))

        StoriTextField(
            value = password,
            label = "Password",
            isPasswordField = true,
            onValueChange = {
                password = it
                isPasswordEmptyError = password.isEmpty()
            },
            onDoneAction = onDoneKeyboardAction,
            isEmptyError = isPasswordEmptyError
        )

        Spacer(modifier = Modifier.height(32.dp))

        StoriButton(
            text = "Login",
            onClick = {
                isPasswordEmptyError = password.isEmpty()
                isEmailEmptyError = email.isEmpty()
                if (!isPasswordEmptyError and !isEmailEmptyError)
                    onLogin(email, password)
            },
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

@Composable
private fun LoadingState(
    showDialog: Boolean = false,
    onDismissRequest: () -> Unit = {},
    isSuccess: Boolean = false,
) {
    Log.d("PERON", "Login Loading state, isLoggingIn: $isSuccess")
    if (showDialog)
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(if (isSuccess) "Signing you in..." else "Loading...") },
            text = { Text("Please wait a moment") },
            confirmButton = { },
            dismissButton = { },
        )
}

@Composable
private fun ErrorState(
    showDialog: Boolean = false,
    onDismissRequest: () -> Unit = {},
    msg: String
) {
    Log.d("PERON", "Login Error state")
    if (showDialog)
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text("Error") },
            text = { Text(msg) },
            confirmButton = { },
            dismissButton = { },
        )
}

@Preview
@Composable
fun LoginPreview() {
    LoginScreen()
}