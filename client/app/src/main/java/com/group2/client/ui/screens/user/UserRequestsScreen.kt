// client/app/src/main/java/com/group2/client/ui/screens/user/UserRequestsScreen.kt (NEW FILE)

package com.group2.client.ui.screens.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.group2.client.model.AdoptionRequest
import com.group2.client.ui.viewmodel.UserViewModel
import androidx.compose.runtime.LaunchedEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRequestsScreen(userViewModel: UserViewModel) {
    val requests: List<AdoptionRequest> by userViewModel.userAdoptionRequests.observeAsState(initial = emptyList())
    val error: String? by userViewModel.error.observeAsState()
    val loading: Boolean by userViewModel.loading.observeAsState(false)
    val userId: Int? by userViewModel.userId.observeAsState()

    // Fetch requests when the screen is displayed and userId is available
    LaunchedEffect(userId) {
        userId?.let {
            userViewModel.getAdoptionRequestsByUserId(it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Adoption Requests") })
        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize(), contentAlignment = Alignment.Center) {
                if (loading) {
                    CircularProgressIndicator()
                } else {
                    error?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                    if (requests.isEmpty()) {
                        Text("You have not made any adoption requests yet.")
                    } else {
                        LazyColumn {
                            items(requests) { request ->
                                UserRequestItem(request = request)
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun UserRequestItem(request: AdoptionRequest) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Dog ID: ${request.dogId}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Request Date: ${request.requestDate}")
            Text(text = "Status: ${request.status}")
        }
    }
}