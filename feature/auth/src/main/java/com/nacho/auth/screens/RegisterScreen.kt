@file:OptIn(ExperimentalComposeUiApi::class)

package com.nacho.auth.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.nacho.auth.components.OnboardingNavButtons
import com.nacho.auth.components.StoriButton
import com.nacho.auth.components.StoriTextField
import com.nacho.auth.viewmodel.AuthViewModel
import com.nacho.model.AuthUiState
import com.nacho.model.User
import kotlinx.coroutines.launch


@Composable
internal fun RegisterRoute(
    onNavigateToLogin: () -> Unit = {},
    onRegisterSuccess: (user: String) -> Unit = { _ -> },
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by authViewModel.uiState.collectAsState()
    RegisterScreen(
        uiState = uiState,
        onNavigateToLogin = onNavigateToLogin,
        onRegister = { user, psw ->
            authViewModel.registerUser(user, psw)
        },
        onRegisterSuccess = {
            onRegisterSuccess(authViewModel.userId)
        }
    )
}

@Composable
internal fun RegisterScreen(
    onNavigateToLogin: () -> Unit = {},
    onRegister: (user: User, password: String) -> Unit = { _, _ -> },
    onRegisterSuccess: () -> Unit = {},
    uiState: AuthUiState
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    when (uiState) {
        AuthUiState.Default -> DefaultState(
            onRegister = onRegister,
            onNavigateToLogin = onNavigateToLogin,
            onDoneKeyboardAction = {
                keyboardController?.hide()
            }
        )

        is AuthUiState.Error -> ErrorState(msg = uiState.msg)
        AuthUiState.Loading -> LoadingState()
        AuthUiState.Success -> {
            LoadingState(showDialog = true, isSuccess = true)
            onRegisterSuccess.invoke()
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DefaultState(
    onNavigateToLogin: () -> Unit = {},
    onRegister: (user: User, password: String) -> Unit = { _, _ -> },
    onDoneKeyboardAction: () -> Unit = {},
) {
    var name by rememberSaveable { mutableStateOf("") }
    var surname by rememberSaveable { mutableStateOf("") }
    var age by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }


    var isNameEmptyError by rememberSaveable { mutableStateOf(false) }
    var isSurnameEmptyError by rememberSaveable { mutableStateOf(false) }
    var isAgeEmptyError by rememberSaveable { mutableStateOf(false) }

    var isEmailEmptyError by rememberSaveable { mutableStateOf(false) }


    var isPasswordError by rememberSaveable { mutableStateOf(false) }
    var isPasswordEmptyError by rememberSaveable { mutableStateOf(false) }

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    val onboardingStepsCount by remember { mutableStateOf(5) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            userScrollEnabled = false,
            pageCount = onboardingStepsCount,
            state = pagerState
        ) { page ->
            when (page) {
                OnboardingPagerStep.Name -> StepOneName(
                    name = name,
                    surname = surname,
                    age = age,
                    isNameEmptyError = isNameEmptyError,
                    isAgeEmptyError = isAgeEmptyError,
                    isSurnameEmptyError = isSurnameEmptyError,
                    onAgeChange = {
                        age = it
                        isAgeEmptyError = age.isEmpty()
                    },
                    onNameChange = {
                        name = it
                        isNameEmptyError = name.isEmpty()
                    },
                    onSurnameChange = {
                        surname = it
                        isSurnameEmptyError = surname.isEmpty()
                    },
                    onBack = { onNavigateToLogin.invoke() },
                    onNext = {
                        isAgeEmptyError = age.isEmpty()
                        isNameEmptyError = name.isEmpty()
                        isSurnameEmptyError = surname.isEmpty()
                        if (!isAgeEmptyError and !isNameEmptyError and !isSurnameEmptyError)
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(
                                    OnboardingPagerStep.Email
                                )
                            }
                    },
                    onDoneKeyboardAction = onDoneKeyboardAction
                )

                OnboardingPagerStep.Email -> StepTwoEmail(
                    email = email,
                    isEmailEmptyError = isEmailEmptyError,
                    onEmailChange = { email = it },
                    onBack = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(
                                OnboardingPagerStep.Name
                            )
                        }
                    },
                    onNext = {
                        isEmailEmptyError = email.isEmpty()
                        if (!isEmailEmptyError)
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(
                                    OnboardingPagerStep.ProfilePicture
                                )
                            }
                    },
                    onDoneKeyboardAction = onDoneKeyboardAction
                )

                OnboardingPagerStep.ProfilePicture -> StepThreeProfilePic(
                    onBack = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(
                                OnboardingPagerStep.Email
                            )
                        }
                    },
                    onNext = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(
                                OnboardingPagerStep.Password
                            )
                        }
                    }
                )

                OnboardingPagerStep.Password -> StepFourPassword(
                    password = password,
                    confirmPassword = confirmPassword,
                    isPasswordError = isPasswordError,
                    isEmptyError = isPasswordEmptyError,
                    onPasswordChange = { password = it },
                    onConfirmPasswordChange = {
                        confirmPassword = it
                        isPasswordError =
                            confirmPassword.isNotEmpty() && confirmPassword != password
                    },
                    onBack = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(
                                OnboardingPagerStep.ProfilePicture
                            )
                        }
                    },
                    onNext = {
                        isPasswordEmptyError = password.isEmpty() || confirmPassword.isEmpty()
                        if (!isPasswordEmptyError)
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(
                                    OnboardingPagerStep.Final
                                )
                            }
                    },
                    onDoneKeyboardAction = onDoneKeyboardAction
                )

                OnboardingPagerStep.Final -> CompleteRegistration(
                    name = name,
                    surname = surname,
                    age = age,
                    imageUri = null,
                    email = email,
                    onNavigateToLogin = onNavigateToLogin,
                    onRegister = {
                        onRegister(it, password)
                    }
                )
            }
        }

    }
}

@Composable
private fun StepOneName(
    name: String,
    surname: String,
    age: String,
    onNameChange: (String) -> Unit = {},
    onSurnameChange: (String) -> Unit = {},
    onAgeChange: (String) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {},
    isNameEmptyError: Boolean = false,
    isSurnameEmptyError: Boolean = false,
    isAgeEmptyError: Boolean = false,
    onDoneKeyboardAction: () -> Unit = {},
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        StepHeadline(title = "Step 1: \nComplete the following data")

        StoriTextField(
            value = name,
            label = "Name",
            onValueChange = onNameChange,
            imeAction = ImeAction.Next,
            onNextAction = { },
            isEmptyError = isNameEmptyError
        )

        Spacer(modifier = Modifier.height(16.dp))

        StoriTextField(
            value = surname,
            label = "Surname",
            onValueChange = onSurnameChange,
            imeAction = ImeAction.Next,
            onNextAction = { },
            isEmptyError = isSurnameEmptyError
        )

        Spacer(modifier = Modifier.height(16.dp))

        StoriTextField(
            value = age,
            label = "Age",
            onValueChange = onAgeChange,
            onDoneAction = onDoneKeyboardAction,
            isNumericalField = true,
            isEmptyError = isAgeEmptyError
        )

        Spacer(modifier = Modifier.height(32.dp))

        OnboardingNavButtons(onNext = onNext, onBack = onBack)
    }
}

@Composable
private fun StepTwoEmail(
    email: String,
    onEmailChange: (String) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {},
    isEmailEmptyError: Boolean = false,
    onDoneKeyboardAction: () -> Unit = {},
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        StepHeadline(title = "Step 2: Enter your email")

        StoriTextField(
            value = email,
            label = "Email",
            onValueChange = onEmailChange,
            onDoneAction = onDoneKeyboardAction,
            isEmailField = true,
            isEmptyError = isEmailEmptyError
        )

        Spacer(modifier = Modifier.height(32.dp))

        OnboardingNavButtons(onNext = onNext, onBack = onBack)
    }
}

@Composable
private fun StepThreeProfilePic(
    onNext: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        StepHeadline(title = "Step 3: \nTake a profile picture")

        StoriButton(text = "Take picture", onClick = {})

        Spacer(modifier = Modifier.height(32.dp))

        OnboardingNavButtons(onBack = onBack, onNext = onNext)
    }
}

@Composable
private fun StepFourPassword(
    password: String,
    confirmPassword: String,
    onPasswordChange: (String) -> Unit = {},
    onConfirmPasswordChange: (String) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {},
    isPasswordError: Boolean = false,
    isEmptyError: Boolean = false,
    onDoneKeyboardAction: () -> Unit = {},
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        StepHeadline(title = "Step 4: Create your password")

        StoriTextField(
            value = password,
            label = "Password",
            onValueChange = onPasswordChange,
            imeAction = ImeAction.Next,
            onNextAction = { },
            isPasswordField = true,
            isEmptyError = isEmptyError
        )

        Spacer(modifier = Modifier.height(24.dp))

        StoriTextField(
            value = confirmPassword,
            label = "Confirm password",
            onValueChange = onConfirmPasswordChange,
            onDoneAction = onDoneKeyboardAction,
            isPasswordField = true,
            isPasswordError = isPasswordError,
            isEmptyError = isEmptyError
        )

        Spacer(modifier = Modifier.height(32.dp))

        OnboardingNavButtons(onNext = onNext, onBack = onBack)
    }
}

@Composable
private fun CompleteRegistration(
    name: String,
    surname: String,
    age: String,
    imageUri: Uri?,
    email: String,
    onNavigateToLogin: () -> Unit = {},
    onRegister: (user: User) -> Unit = { _ -> }
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        StepHeadline(title = "Complete Registration")

        // Display the registration summary
        Text("Name: $name", style = MaterialTheme.typography.labelLarge)
        Text("Surname: $surname", style = MaterialTheme.typography.labelLarge)
        Text("Age: $age", style = MaterialTheme.typography.labelLarge)
        Text("Email: $email", style = MaterialTheme.typography.labelLarge)

        Spacer(modifier = Modifier.height(32.dp))

        StoriButton(
            text = "Register",
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val user = User(
                    email = email,
                    userName = "$name $surname",
                    age = age,
                    idUrl = "",
                    movements = emptyList()
                )
                onRegister(user)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        StoriButton(
            text = "Back to login",
            onClick = { onNavigateToLogin.invoke() },
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
    Log.d("PERON", "Register Loading state")
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
    Log.d("PERON", "Register Error state")
    if (showDialog)
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text("Error") },
            text = { Text(msg) },
            confirmButton = { },
            dismissButton = { },
        )
}

@Composable
fun StepHeadline(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.headlineMedium
    )
    Spacer(modifier = Modifier.height(24.dp))
}

@Preview(showBackground = true)
@Composable
fun StepOneName() {
    StepOneName(name = "Ignacio", surname = "Centola", age = "23")
}

@Preview(showBackground = true)
@Composable
fun StepTwoEmail() {
    StepTwoEmail(email = "centola@gmail.com")
}

@Preview(showBackground = true)
@Composable
fun StepFourPassword() {
    StepFourPassword(password = "", confirmPassword = "")
}

@Preview(showBackground = true)
@Composable
fun CompleteRegis() {
    CompleteRegistration(
        name = "Ignacio",
        surname = "Centola",
        age = "23",
        imageUri = null,
        email = "centola@gmail.com",
    )
}

private object OnboardingPagerStep {
    const val Name = 0
    const val Email = 1
    const val ProfilePicture = 2
    const val Password = 3
    const val Final = 4
}
