package com.nacho.auth.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoriTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit = {},
    imeAction: ImeAction = ImeAction.Done,
    onNextAction: () -> Unit = {},
    onDoneAction: () -> Unit = {},
    isPasswordField: Boolean = false,
    isNumericalField: Boolean = false,
    isEmailField: Boolean = false,
    isPasswordError: Boolean = false,
    isEmptyError: Boolean = false,
) {

    var showPassword by remember {
        mutableStateOf(isPasswordField.not())
    }

    val keyboardType = if (isNumericalField) {
        KeyboardType.Number
    } else {
        if (isEmailField) KeyboardType.Email else KeyboardType.Text
    }

    val leadingIcon = if (isEmailField) {
        Icons.Default.Email
    } else if (isPasswordField) {
        Icons.Default.Lock
    } else {
        Icons.Default.Person
    }


    OutlinedTextField(
        value = value,
        singleLine = true,
        onValueChange = { onValueChange(it) },
        label = { Text(label) },
        leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = null) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = keyboardType
        ),
        keyboardActions = KeyboardActions(
            onNext = { onNextAction.invoke() },
            onDone = { onDoneAction.invoke() }
        ),
        shape = RoundedCornerShape(percent = 20),
        supportingText = {
            Text(
                text = if (isPasswordError)
                    "Passwords do not match"
                else if (isEmptyError) "Complete this field"
                else ""
            )
        },
        isError = isPasswordError or isEmptyError,
        trailingIcon = {
            if (isPasswordField)
                if (showPassword) {
                    IconButton(onClick = { showPassword = false }) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = "hide_password"
                        )
                    }
                } else {
                    IconButton(
                        onClick = { showPassword = true }) {
                        Icon(
                            imageVector = Icons.Filled.VisibilityOff,
                            contentDescription = "hide_password"
                        )
                    }
                }
        },
        visualTransformation = if (showPassword)
            VisualTransformation.None else PasswordVisualTransformation()
    )

}