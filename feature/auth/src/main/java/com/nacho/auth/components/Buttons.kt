package com.nacho.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


@Composable
private fun NextButton(onNext: () -> Unit) {
    StoriButton(onClick = onNext, text = "Next")
}

@Composable
private fun PreviousButton(onBack: () -> Unit) {
    StoriButton(onClick = onBack, text = "Previous", isOutlined = true)
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

@Composable
fun StoriButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit = {},
    isOutlined: Boolean = false,
    icon: ImageVector? = null
) {
    if (isOutlined) {
        OutlinedButton(onClick = onClick, shape = RoundedCornerShape(8.dp), modifier = modifier) {
            icon?.let {
                Icon(imageVector = icon, contentDescription = "Camera")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, style = MaterialTheme.typography.labelMedium)
        }
    } else {
        Button(onClick = onClick, shape = RoundedCornerShape(8.dp), modifier = modifier) {
            icon?.let {
                Icon(imageVector = icon, contentDescription = "Camera")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, style = MaterialTheme.typography.labelMedium)
        }
    }
}