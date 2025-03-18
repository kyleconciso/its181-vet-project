// client/app/src/main/java/com/group2/client/ui/screens/user/DogDetailScreenUser.kt (Corrected)
package com.group2.client.ui.screens.user

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.group2.client.api.RetrofitClient
import com.group2.client.model.AdoptionRequest
import com.group2.client.model.Dog
import com.group2.client.ui.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import coil.compose.AsyncImage
import androidx.compose.runtime.livedata.observeAsState // Correct import


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogDetailScreenUser(dogId: Long, navController: NavController, userViewModel: UserViewModel) {
    val coroutineScope = rememberCoroutineScope()
    var dog by remember { mutableStateOf<Dog?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val userId: Int? by userViewModel.userId.observeAsState() // Keep as Int?
    val userError by userViewModel.error.observeAsState()


    LaunchedEffect(dogId) {
        coroutineScope.launch {
            isLoading = true
            error = null
            try {
                val response = RetrofitClient.instance.getDogById(dogId) // Use dogId
                if (response.isSuccessful) {
                    dog = response.body()?.data
                } else {
                    error = response.body()?.message ?: "Error fetching dog: ${response.code()}"
                }
            } catch (e: Exception) {
                error = "Network error: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Dog Details") })
        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize(), contentAlignment = Alignment.Center) { // Center content
                if (isLoading) {
                    CircularProgressIndicator()
                } else if (error != null) {
                    Text(error!!)
                } else if (userError != null){
                    Text(userError!!, color = MaterialTheme.colorScheme.error) //Correct Text
                }
                else if (dog != null) {
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

                        Button(onClick = {
                            // Create an adoption request, use let to handle null userId
                            userId?.let { uid ->
                                val adoptionRequest = AdoptionRequest(
                                    dogId = dog!!.id!!,
                                    userId = uid, // Use the non-null 'uid'
                                    userEmail = "",
                                )

                                coroutineScope.launch {
                                    userViewModel.createAdoptionRequest(adoptionRequest)
                                }
                                navController.popBackStack()
                            } ?: run {
                                // Handle null userId (shouldn't happen if logged in)
                                error = "User ID is null. Please log in again."
                            }

                        }, enabled = !(userViewModel.loading.value ?: false)) { // Corrected enabled check
                            Text("Adopt")
                        }
                    }
                } else {
                    Text("Dog not found")
                }
            }
        }
    )
}