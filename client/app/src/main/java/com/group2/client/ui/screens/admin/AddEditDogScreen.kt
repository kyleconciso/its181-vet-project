// client/app/src/main/java/com/group2/client/ui/screens/admin/AddEditDogScreen.kt

package com.group2.client.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.group2.client.api.RetrofitClient
import com.group2.client.model.Dog
import com.group2.client.ui.viewmodel.AdminViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.livedata.observeAsState // Correct import


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditDogScreen(dogId: Long, navController: NavController, adminViewModel: AdminViewModel) {
    val coroutineScope = rememberCoroutineScope()
    // Initialize with a null id for new dogs, or fetch existing dog data
    var dog by remember { mutableStateOf(Dog(null, "", "", 0, "", "", "", true)) }
    var isLoading by remember { mutableStateOf(false) }
    var isEditMode by remember{ mutableStateOf(false)}
    val error by adminViewModel.error.observeAsState()

    LaunchedEffect(dogId) {
        if (dogId != -1L) {
            isLoading = true
            isEditMode = true;
            coroutineScope.launch {
                try {
                    val response = RetrofitClient.instance.getDogById(dogId)
                    if (response.isSuccessful) {
                        // If successful, update the `dog` state with the fetched data
                        response.body()?.data?.let{
                            dog = it.copy(id = it.id) // Ensure you're using Int? correctly
                        }
                    } else {
                        adminViewModel.getAllDogs() // Consider more specific error handling
                        println("Error fetching dog: ${response.message()}")
                    }
                } catch (e: Exception) {
                    println("Network error: ${e.localizedMessage}")
                } finally {
                    isLoading = false
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (isEditMode) "Edit Dog" else "Add Dog") })
        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        error?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        OutlinedTextField(
                            value = dog.name,
                            onValueChange = { dog = dog.copy(name = it) },
                            label = { Text("Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = dog.breed,
                            onValueChange = { dog = dog.copy(breed = it) },
                            label = { Text("Breed") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = dog.age.toString(),
                            onValueChange = { dog = dog.copy(age = it.toIntOrNull() ?: 0) },
                            label = { Text("Age") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = dog.gender,
                            onValueChange = { dog = dog.copy(gender = it) },
                            label = { Text("Gender") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = dog.description,
                            onValueChange = { dog = dog.copy(description = it) },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = dog.imageUrl,
                            onValueChange = { dog = dog.copy(imageUrl = it) },
                            label = { Text("Image URL") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = dog.isAvailable,
                                onCheckedChange = { dog = dog.copy(isAvailable = it) }
                            )
                            Text("Available")
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            isLoading = true
                            coroutineScope.launch {
                                if (isEditMode) {
                                    // Ensure we send the correct ID for updates
                                    dog.id?.let{
                                        adminViewModel.updateDog(dog.copy(id= it))

                                    }
                                } else {
                                    adminViewModel.createDog(dog)
                                }
                                navController.popBackStack()
                                isLoading = false
                            }

                        }) {
                            Text(if (isEditMode) "Update Dog" else "Add Dog")
                        }
                    }
                }
            }
        }
    )
}