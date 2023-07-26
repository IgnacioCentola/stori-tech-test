@file:OptIn(ExperimentalMaterial3Api::class)

package com.nacho.auth

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
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RegisterScreen(onNavigateToLogin: () -> Unit = {}) {
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

        val pagerState = rememberPagerState()
        val coroutineScope = rememberCoroutineScope()

        val onboardingStepsCount by remember { mutableStateOf(4) }



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
                    OnboardingScreens.Name -> StepOne(
                        name = name,
                        surname = surname,
                        age = age,
                        onAgeChange = { age = it },
                        onNameChange = { name = it },
                        onSurnameChange = { surname = it },
                        onBack = { onNavigateToLogin.invoke() },
                        onNext = {
                            coroutineScope.launch { pagerState.animateScrollToPage(OnboardingScreens.Email) }
                        }
                    )

                    OnboardingScreens.Email -> StepTwo(
                        email = email,
                        onEmailChange = { email = it },
                        onBack = {
                            coroutineScope.launch { pagerState.animateScrollToPage(OnboardingScreens.Name) }
                        },
                        onNext = {
                            coroutineScope.launch { pagerState.animateScrollToPage(OnboardingScreens.Password) }
                        })

                    OnboardingScreens.Password -> StepThree(
                        password = password,
                        confirmPassword = confirmPassword,
                        onPasswordChange = { password = it },
                        onConfirmPasswordChange = { confirmPassword = it },
                        onBack = {
                            coroutineScope.launch { pagerState.animateScrollToPage(OnboardingScreens.Email) }
                        },
                        onNext = {
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
                        email = email
                    ) {
                        onNavigateToLogin.invoke()
                    }
                }
            }

        }
    }
}

@Composable
fun StepOne(
    name: String,
    surname: String,
    age: String,
    onNameChange: (String) -> Unit = {},
    onSurnameChange: (String) -> Unit = {},
    onAgeChange: (String) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        StepHeadline(title = "Step 1: \nComplete the following data")

        StoriTextField(
            value = name,
            label = "Name",
            onValueChange = onNameChange,
            imeAction = ImeAction.Next,
            onNextAction = { }
        )

        Spacer(modifier = Modifier.height(16.dp))

        StoriTextField(
            value = surname,
            label = "Surname",
            onValueChange = onSurnameChange,
            imeAction = ImeAction.Next,
            onNextAction = { }
        )

        Spacer(modifier = Modifier.height(16.dp))

        StoriTextField(
            value = age,
            label = "Age",
            onValueChange = onAgeChange,
            onDoneAction = { }, isNumericalField = true)

        Spacer(modifier = Modifier.height(32.dp))

        OnboardingNavButtons(onNext = onNext, onBack = onBack)
    }
}

@Composable
fun StepTwo(
    email: String,
    onEmailChange: (String) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        StepHeadline(title = "Step 2: Enter your email")

        StoriTextField(
            value = email,
            label = "Email",
            onValueChange = onEmailChange,
            onDoneAction = {  }, isEmailField = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OnboardingNavButtons(onNext = onNext, onBack = onBack)
    }
}


@Composable
fun StepThree(
    password: String,
    confirmPassword: String,
    onPasswordChange: (String) -> Unit = {},
    onConfirmPasswordChange: (String) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        StepHeadline(title = "Step 3: Create your password")

        StoriTextField(
            value = password,
            label = "Password",
            onValueChange = onPasswordChange,
            imeAction = ImeAction.Next,
            onNextAction = { },
            isPasswordField = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        StoriTextField(
            value = confirmPassword,
            label = "Confirm password",
            onValueChange = onConfirmPasswordChange,
            onDoneAction = { },
            isPasswordField = true
        )

        Spacer(modifier = Modifier.height(16.dp))

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
    onNavigateToLogin: () -> Unit
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
            onClick = {}
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
fun StepOne() {
    StepOne(name = "Ignacio", surname = "Centola", age = "23")
}

@Preview(showBackground = true)
@Composable
fun StepTwo() {
    StepTwo(email = "centola@gmail.com")
}

@Preview(showBackground = true)
@Composable
fun StepThree() {
    StepThree(password = "", confirmPassword = "")
}

@Preview(showBackground = true)
@Composable
fun CompleteRegis() {
    CompleteRegistration(
        name = "Ignacio",
        surname = "Centola",
        age = "23",
        imageUri = null,
        email = "centola@gmail.com"
    ) {

    }
}
