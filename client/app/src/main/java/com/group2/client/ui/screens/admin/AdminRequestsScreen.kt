// client/app/src/main/java/com/group2/client/ui/screens/admin/AdminRequestsScreen.kt

package com.group2.client.ui.screens.admin

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
import com.group2.client.ui.viewmodel.AdminViewModel
import androidx.compose.runtime.LaunchedEffect // Import LaunchedEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRequestsScreen(adminViewModel: AdminViewModel) {
    val requests: List<AdoptionRequest> by adminViewModel.requests.observeAsState(initial = emptyList())
    val error: String? by adminViewModel.error.observeAsState()
    val loading: Boolean by adminViewModel.loading.observeAsState(false)

    // Use LaunchedEffect to fetch requests when the screen is displayed
    LaunchedEffect(Unit) {
        adminViewModel.getAllAdoptionRequests()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Adoption Requests") })
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
                        Text("No adoption requests found.")
                    } else {
                        LazyColumn {
                            items(requests) { request ->
                                RequestItem(request = request, adminViewModel = adminViewModel)
                            }
                        }
                    }
                }
            }
        }
    )
}


@Composable
fun RequestItem(request: AdoptionRequest, adminViewModel: AdminViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Dog ID: ${request.dogId}", style = MaterialTheme.typography.titleMedium)
            Text(text = "User ID: ${request.userId}")
            Text(text = "Request Date: ${request.requestDate}")
            Text(text = "Status: ${request.status}")
            Spacer(modifier = Modifier.height(8.dp))

            // Buttons to update the status
            Row {
                Button(
                    onClick = {
                        request.id?.let { // Safely use the request ID
                            adminViewModel.updateRequestStatus(it, "APPROVED")
                        }
                    },
                    enabled = request.status != "APPROVED" // Disable if already approved
                ) {
                    Text("Approve")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        request.id?.let { // Safely use the request ID
                            adminViewModel.updateRequestStatus(it, "REJECTED")
                        }
                    },
                    enabled = request.status != "REJECTED" // Disable if already rejected
                ) {
                    Text("Reject")
                }
            }
        }
    }
}