package com.nacho.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nacho.model.AuthUiState
import com.nacho.model.Movement
import com.nacho.model.User


@Composable
fun HomeScreen(uiState: AuthUiState) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (uiState) {
            is AuthUiState.Error -> ErrorState(uiState.msg)
            AuthUiState.Loading -> LoadingState()
            is AuthUiState.Success -> SuccessState(uiState.user)
        }
    }
}

@Composable
fun SuccessState(user: User) {
    Log.d("PERON", "Success state")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        UserInfoCard(email = user.email.toString(), userAge = user.age.toString())
        Spacer(modifier = Modifier.height(16.dp))
        UserTransactionsCard(
            movements = user.movements
        )
    }
}

@Composable
fun LoadingState() {
    Log.d("PERON", "Loading state")
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(strokeWidth = 5.dp)
    }
}

@Composable
fun ErrorState(errorMsg: String) {
    Log.d("PERON", "Error state")
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = errorMsg)
    }
}

@Composable
fun UserInfoCard(email: String, userAge: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "User Information",
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Name: $email")
            Text(text = "Age: $userAge")
        }
    }
}

@Composable
fun UserTransactionsCard(movements: List<Movement>) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "User Transactions",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            movements.forEach {
                Text(text = it.amount.toString())
            }
        }
    }
}