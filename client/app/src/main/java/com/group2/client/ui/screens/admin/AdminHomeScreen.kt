// client/app/src/main/java/com/group2/client/ui/screens/admin/AdminHomeScreen.kt
package com.group2.client.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.group2.client.ui.components.DogCard
import com.group2.client.ui.navigation.Screen
import com.group2.client.ui.viewmodel.AdminViewModel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.group2.client.ui.viewmodel.SharedViewModel
import androidx.compose.runtime.LaunchedEffect
import com.group2.client.model.Dog
import androidx.compose.ui.Alignment


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(navController: NavController, adminViewModel: AdminViewModel, sharedViewModel: SharedViewModel){
    val dogs: List<Dog> by adminViewModel.dogs.observeAsState(initial = emptyList())
    val error: String? by adminViewModel.error.observeAsState(initial = null)
    val loading: Boolean by adminViewModel.loading.observeAsState(false)

    val username by sharedViewModel.username.observeAsState() // Get username


    // Fetch all dogs when the screen is created
    LaunchedEffect(Unit) {
        adminViewModel.getAllDogs()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if(username != null) "Welcome, $username" else "Admin Home") },
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
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp), // Space between buttons
            ) {
                FloatingActionButton(onClick = { navController.navigate(Screen.AdminRequests.route) }) {
                    Text("Requests") // Text instead of an icon
                }
                FloatingActionButton(onClick = { navController.navigate(Screen.AddEditDog.createRoute(-1L)) }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Dog")
                }
            }

        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize(), contentAlignment = Alignment.Center){ // Center content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    if (loading) {
                        CircularProgressIndicator()
                    } else {
                        error?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error)
                        }

                        if (dogs.isEmpty()) {
                            Text("No dogs found.")
                        } else {
                            LazyColumn {
                                items(dogs) { dog ->
                                    DogCard(dog = dog) {
                                        dog.id?.let{
                                            navController.navigate(Screen.DogDetailAdmin.createRoute(it.toLong()))
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