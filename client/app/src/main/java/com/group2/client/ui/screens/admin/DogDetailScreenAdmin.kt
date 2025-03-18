// client/app/src/main/java/com/group2/client/ui/screens/admin/DogDetailScreenAdmin.kt

package com.group2.client.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.group2.client.api.RetrofitClient
import com.group2.client.model.Dog
import com.group2.client.ui.navigation.Screen
import kotlinx.coroutines.launch
import coil.compose.AsyncImage
import com.group2.client.ui.viewmodel.AdminViewModel
import androidx.compose.runtime.livedata.observeAsState // Correct import


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogDetailScreenAdmin(dogId: Long, navController: NavController, adminViewModel: AdminViewModel) {
    val coroutineScope = rememberCoroutineScope()
    var dog by remember { mutableStateOf<Dog?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val adminError by adminViewModel.error.observeAsState() // observeAsState for LiveData

    LaunchedEffect(dogId) {
        coroutineScope.launch {
            isLoading = true
            error = null // Clear previous errors
            try{
                val response = RetrofitClient.instance.getDogById(dogId) // Use dogId
                if (response.isSuccessful) {
                    dog = response.body()?.data
                } else {
                    error = response.body()?.message ?: "Error fetching dog: ${response.code()}"
                }
            } catch (e: Exception){
                error = "Network error: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dog Details") },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.AddEditDog.createRoute(dogId)) // Navigate to edit screen
                    }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit Dog")
                    }
                    IconButton(onClick = {
                        coroutineScope.launch {
                            adminViewModel.deleteDog(dogId.toInt()) //delete dog, Use dogId.toInt()
                        }
                        navController.popBackStack() // Go back after deletion
                    }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete Dog")
                    }
                }
            )
        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize(), contentAlignment = Alignment.Center) { // Center content
                if (isLoading) {
                    CircularProgressIndicator()
                } else if (error != null) {
                    Text(error!!)
                } else if (adminError != null) { // Display ViewModel errors
                    Text(adminError!!, color = MaterialTheme.colorScheme.error) //Correct Text
                }else if (dog != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        AsyncImage(
                            model = dog!!.imageUrl,
                            contentDescription = "Dog Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Name: ${dog!!.name}", style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Breed: ${dog!!.breed}")
                        Text("Age: ${dog!!.age}")
                        Text("Gender: ${dog!!.gender}")
                        Text("Description: ${dog!!.description}")
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }else {
                    Text("Dog not found")
                }
            }
        }
    )
}