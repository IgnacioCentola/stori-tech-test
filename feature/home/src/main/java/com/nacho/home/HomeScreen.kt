package com.nacho.home

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.Timestamp
import com.nacho.home.viewmodel.HomeViewModel
import com.nacho.model.HomeUiState
import com.nacho.model.Movement
import com.nacho.model.User
import kotlinx.coroutines.launch
import java.util.Date


@Composable
internal fun HomeRoute(
    homeViewModel: HomeViewModel = hiltViewModel(),
    userId: String = ""
) {
    Log.d("PERON", "HomeRoute called with userId: $userId")
    val coroutineScope = rememberCoroutineScope()
    val state by homeViewModel.uiState.collectAsState()
    LaunchedEffect(key1 = userId) {
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
        UserInfoCard(user)
        Spacer(modifier = Modifier.height(16.dp))
        UserTransactionsCard(movements = user.movements)

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
private fun UserInfoCard(
    user: User
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "User Information",
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.height(24.dp))
            if (user.idImageUrl.isNullOrEmpty().not()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(user.idImageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "user_profile_pic",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.clip(CircleShape)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Name: ${user.userName}", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Email: ${user.email}", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Age: ${user.age}", style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun UserTransactionsCard(movements: List<Movement>?) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(),
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
                text = "Your transactions",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            movements?.let {
                LazyColumn {
                    items(it) { movement ->
                        TransactionCard(movement = movement)
                    }
                }
            } ?: Text(text = "You have no transactions yet")
        }
    }
}

@Composable
fun TransactionCard(movement: Movement) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(),

        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "Transaction ID: #${movement.id}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Amount: ${movement.amount}", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Date: ${movement.date}", style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserCardPreview() {
    SuccessState(
        user = User(
            userName = "Test Username",
            age = "23",
            email = "mail@mail.com",
//            idImageUrl = "https://img.freepik.com/free-photo/happiness-wellbeing-confidence-concept-cheerful-attractive-african-american-woman-curly-haircut-cross-arms-chest-self-assured-powerful-pose-smiling-determined-wear-yellow-sweater_176420-35063.jpg",
            movements = listOf(
                Movement(
                    id = "1",
                    amount = 100,
                    date = Timestamp(Date()),
                    receiver = "Another user"
                )
            )
        )
    )
}