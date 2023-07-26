package com.nacho.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NextButton(onNext: () -> Unit) {
    Button(
        onClick = { onNext.invoke() }
    ) {
        Text("Next")
    }
}

@Composable
fun PreviousButton(onBack: () -> Unit) {
    Button(
        onClick = { onBack.invoke() }
    ) {
        Text("Back")
    }
}

@Composable
fun OnboardingNavButtons(
    onBack: () -> Unit = {},
    onNext: () -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        PreviousButton {
            onBack.invoke()
        }
        NextButton {
            onNext.invoke()
        }
    }
}