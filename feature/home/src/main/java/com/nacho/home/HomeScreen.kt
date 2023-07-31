package com.nacho.home

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nacho.home.viewmodel.HomeViewModel
import com.nacho.model.HomeUiState
import com.nacho.model.Movement
import com.nacho.model.User
import kotlinx.coroutines.launch


@Composable
internal fun HomeRoute(
    homeViewModel: HomeViewModel = hiltViewModel(),
    userId: String = ""
) {
    Log.d("PERON", "HomeRoute called with userId: $userId")
    val coroutineScope = rememberCoroutineScope()
    val state by homeViewModel.uiState.collectAsState()
    LaunchedEffect(key1 = userId){
        coroutineScope.launch {
            homeViewModel.getUserData(userId)
        }
    }
    HomeScreen(state = state)
}

@Composable
internal fun HomeScreen(
    state: HomeUiState
) {
    when (state) {
        is HomeUiState.Error -> ErrorState(errorMsg = state.msg)
        HomeUiState.Loading -> LoadingState()
        is HomeUiState.Success -> SuccessState(user = state.user)
    }

}

@Composable
private fun SuccessState(user: User) {
    Log.d("PERON", "Home screen Success state")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        UserInfoCard(email = user.email.toString(), userAge = user.age.toString())
        Spacer(modifier = Modifier.height(16.dp))
        user.movements?.let {
            UserTransactionsCard(
                movements = it
            )
        }
    }
}

@Composable
private fun LoadingState() {
    Log.d("PERON", "Home screen Loading state")
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Loading user data...") },
        text = { Text("Please wait a moment") },
        confirmButton = { },
        dismissButton = { },
    )
}

@Composable
private fun ErrorState(errorMsg: String) {
    Log.d("PERON", "Home screen Error state")
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Error") },
        text = { Text(errorMsg) },
        confirmButton = { },
        dismissButton = { },
    )
}

@Composable
private fun UserInfoCard(email: String, userAge: String) {
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
private fun UserTransactionsCard(movements: List<Movement>) {
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