package com.nacho.auth.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
    isEmailField: Boolean = false
) {

    val keyboardType = if (isNumericalField) {
        KeyboardType.Number
    } else {
        if (isEmailField) KeyboardType.Email else KeyboardType.Text
    }

    val icon = if (isEmailField) {
       Icons.Default.Email
    } else if (isPasswordField) {
        Icons.Default.Lock
    } else {
        Icons.Default.Person
    }

    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(label) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = null) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = keyboardType
        ),
        keyboardActions = KeyboardActions(
            onNext = { onNextAction.invoke() },
            onDone = { onDoneAction.invoke() }
        ),
        visualTransformation = if (isPasswordField) PasswordVisualTransformation() else VisualTransformation.None
    )

}