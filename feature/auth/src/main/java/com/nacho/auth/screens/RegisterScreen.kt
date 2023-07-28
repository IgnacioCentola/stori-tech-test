@file:OptIn(ExperimentalMaterial3Api::class)

package com.nacho.auth.screens

import android.net.Uri
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nacho.auth.components.OnboardingNavButtons
import com.nacho.auth.components.StoriButton
import com.nacho.auth.components.StoriTextField
import com.nacho.auth.navigation.OnboardingScreens
import com.nacho.model.User
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit = {},
    onRegister: (user: User, password: String) -> Unit = { _, _ -> }
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
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
                    OnboardingScreens.Name -> StepOneName(
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
                                        OnboardingScreens.Email
                                    )
                                }
                        }
                    )

                    OnboardingScreens.Email -> StepTwoEmail(
                        email = email,
                        isEmailEmptyError = isEmailEmptyError,
                        onEmailChange = { email = it },
                        onBack = {
                            coroutineScope.launch { pagerState.animateScrollToPage(OnboardingScreens.Name) }
                        },
                        onNext = {
                            isEmailEmptyError = email.isEmpty()
                            if (!isEmailEmptyError)
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(
                                        OnboardingScreens.ProfilePicture
                                    )
                                }
                        })

                    OnboardingScreens.ProfilePicture -> StepThreeProfilePic(
                        onBack = {
                            coroutineScope.launch { pagerState.animateScrollToPage(OnboardingScreens.Email) }
                        },
                        onNext = {
                            coroutineScope.launch { pagerState.animateScrollToPage(OnboardingScreens.Password) }
                        }
                    )

                    OnboardingScreens.Password -> StepFourPassword(
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
                            coroutineScope.launch { pagerState.animateScrollToPage(OnboardingScreens.ProfilePicture) }
                        },
                        onNext = {
                            isPasswordEmptyError = password.isEmpty() || confirmPassword.isEmpty()
                            if (!isPasswordEmptyError)
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(
                                        OnboardingScreens.Final
                                    )
                                }
                        })

                    OnboardingScreens.Final -> CompleteRegistration(
                        name = name,
                        surname = surname,
                        age = age,
                        imageUri = null,
                        email = email,
                        password = password,
                        onNavigateToLogin = onNavigateToLogin,
                        onRegister = onRegister
                    )
                }
            }

        }
    }
}

@Composable
fun StepOneName(
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
            onDoneAction = { },
            isNumericalField = true,
            isEmptyError = isAgeEmptyError
        )

        Spacer(modifier = Modifier.height(32.dp))

        OnboardingNavButtons(onNext = onNext, onBack = onBack)
    }
}

@Composable
fun StepTwoEmail(
    email: String,
    onEmailChange: (String) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {},
    isEmailEmptyError: Boolean = false,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        StepHeadline(title = "Step 2: Enter your email")

        StoriTextField(
            value = email,
            label = "Email",
            onValueChange = onEmailChange,
            onDoneAction = { },
            isEmailField = true,
            isEmptyError = isEmailEmptyError
        )

        Spacer(modifier = Modifier.height(32.dp))

        OnboardingNavButtons(onNext = onNext, onBack = onBack)
    }
}

@Composable
fun StepThreeProfilePic(
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
fun StepFourPassword(
    password: String,
    confirmPassword: String,
    onPasswordChange: (String) -> Unit = {},
    onConfirmPasswordChange: (String) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {},
    isPasswordError: Boolean = false,
    isEmptyError: Boolean = false,
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
            onDoneAction = { },
            isPasswordField = true,
            isPasswordError = isPasswordError,
            isEmptyError = isEmptyError
        )

        Spacer(modifier = Modifier.height(32.dp))

        OnboardingNavButtons(onNext = onNext, onBack = onBack)
    }
}

@Composable
fun CompleteRegistration(
    name: String,
    surname: String,
    age: String,
    imageUri: Uri?,
    email: String,
    password: String,
    onNavigateToLogin: () -> Unit = {},
    onRegister: (user: User, password: String) -> Unit = { _, _ -> }
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
                    age = age
                )
                onRegister(user, password)
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
        password = ""
    )
}
