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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nacho.auth.components.OnboardingNavButtons
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RegisterScreen(onNavigateToLogin: () -> Unit = {}) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        val pagerState = rememberPagerState()
        var name by rememberSaveable { mutableStateOf("") }
        var surname by rememberSaveable { mutableStateOf("") }
        var age by rememberSaveable { mutableStateOf("") }
        var email by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }
        var confirmPassword by rememberSaveable { mutableStateOf("") }

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
                    0 -> StepOne(
                        name = name,
                        surname = surname,
                        age = age,
                        onAgeChange = { age = it },
                        onNameChange = { name = it },
                        onSurnameChange = { surname = it },
                        onBack = { onNavigateToLogin.invoke() },
                        onNext = {
                            coroutineScope.launch { pagerState.animateScrollToPage(1) }
                        }
                    )

                    1 -> StepTwo(
                        email = email,
                        onEmailChange = { email = it },
                        onBack = {
                            coroutineScope.launch { pagerState.animateScrollToPage(0) }
                        },
                        onNext = {
                            coroutineScope.launch { pagerState.animateScrollToPage(2) }
                        })

                    2 -> StepThree(
                        password = password,
                        confirmPassword = confirmPassword,
                        onPasswordChange = { password = it },
                        onConfirmPasswordChange = { confirmPassword = it },
                        onBack = {
                            coroutineScope.launch { pagerState.animateScrollToPage(1) }
                        },
                        onNext = { coroutineScope.launch { pagerState.animateScrollToPage(3) } })

                    3 -> CompleteRegistration(
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
        Text(
            "Step 1: \nComplete the following data",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = name,
            onValueChange = { onNameChange(it) },
            label = { Text("Name") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { /* Handle Next action */ }
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = surname,
            onValueChange = { onSurnameChange(it) },
            label = { Text("Surname") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { /* Handle Next action */ }
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = age,
            onValueChange = { onAgeChange(it) },
            label = { Text("Age") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {}
            )
        )


        Spacer(modifier = Modifier.height(16.dp))

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
        Text("Step 2: Enter your email", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { onEmailChange(it) },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { /* Handle Next action */ }
            )
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
        Text("Step 3: Create your password", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password,
            onValueChange = { onPasswordChange(it) },
            trailingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { /* Handle Next action */ }
            ),
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors(
//                    backgroundColor = Color.Transparent,
                textColor = MaterialTheme.colorScheme.onSurface
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = confirmPassword,
            onValueChange = { onConfirmPasswordChange(it) },
            trailingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) },
            label = { Text("Confirm Password") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { /* Handle Done action */ }
            ),
            visualTransformation = PasswordVisualTransformation(),
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
        Text("Complete Registration", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Display the registration summary
        Text("Name: $name")
        Text("Surname: $surname")
        Text("Age: $age")
        Text("Email: $email")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Handle register button click */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                onNavigateToLogin.invoke() // Navigate back to the LoginScreen
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Login", fontSize = 18.sp)
        }
    }
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
