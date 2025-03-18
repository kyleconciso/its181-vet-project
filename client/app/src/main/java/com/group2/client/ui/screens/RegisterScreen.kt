//client/app/src/main/java/com/group2/client/ui/screens/RegisterScreen.kt
package com.group2.client.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.group2.client.ui.viewmodel.SharedViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.input.PasswordVisualTransformation


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("USER") } // Default to USER
    val loading: Boolean by sharedViewModel.loading.observeAsState(false)
    val registrationError by sharedViewModel.registrationError.observeAsState()  // Observe the error


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Register", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = role == "USER",
                onClick = { role = "USER" }
            )
            Text("User")

            RadioButton(
                selected = role == "ADMIN",
                onClick = { role = "ADMIN" }
            )
            Text("Admin")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Display registration errors
        registrationError?.let { error ->
            Text(error, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (loading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                if (password != confirmPassword) {
                    // You should show an error message here, not call register
                    sharedViewModel.register(
                        mapOf( // Pass all data
                            "username" to username,
                            "email" to email,
                            "password" to password,
                            "role" to role
                        )
                    )
                    return@Button // Stop execution if passwords don't match
                }

                val userData = mapOf(
                    "username" to username,
                    "email" to email,
                    "password" to password,
                    "role" to role
                )
                sharedViewModel.register(userData)
            },
            enabled = !loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
    }
}