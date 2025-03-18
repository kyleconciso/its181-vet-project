
// client/app/src/main/java/com/group2/client/ui/screens/user/UserHomeScreen.kt (Modified)
package com.group2.client.ui.screens.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.group2.client.ui.components.DogCard
import com.group2.client.ui.viewmodel.UserViewModel
import com.group2.client.ui.navigation.Screen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import com.group2.client.model.Dog
import com.group2.client.ui.viewmodel.SharedViewModel
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHomeScreen(navController: NavController, userViewModel: UserViewModel, sharedViewModel: SharedViewModel){
    val availableDogs: List<Dog> by userViewModel.availableDogs.observeAsState(initial = emptyList())
    val error: String? by userViewModel.error.observeAsState(initial = null)
    val loading: Boolean by userViewModel.loading.observeAsState(false)

    val username by sharedViewModel.username.observeAsState() // Get username


    // Fetch available dogs when the screen is created
    LaunchedEffect(Unit) {
        userViewModel.getAvailableDogs()
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if(username != null) "Welcome, $username" else "Available Dogs") },
                actions = {
                    IconButton(onClick = {
                        sharedViewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            //Add request button and add dog button, horizontally
            FloatingActionButton(onClick = { navController.navigate(Screen.UserRequests.route)}) { // Added FAB
                Text("My Requests")
            }
        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize(), contentAlignment = Alignment.Center) { // Center content

                if (loading) {
                    CircularProgressIndicator()
                }
                else{
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        error?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error) // Overload resolution ambiguity
                        }

                        if (availableDogs.isEmpty()) {
                            Text("No dogs available at the moment.")
                        } else {
                            LazyColumn {
                                items(availableDogs) { dog ->
                                    DogCard(dog = dog) {
                                        dog.id?.let{
                                            navController.navigate(Screen.DogDetailUser.createRoute(it.toLong()))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    )
}