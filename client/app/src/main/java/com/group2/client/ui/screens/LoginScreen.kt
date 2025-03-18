package com.group2.client.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import com.group2.client.ui.viewmodel.SharedViewModel
import androidx.compose.runtime.livedata.observeAsState
import com.group2.client.ui.navigation.Screen
import androidx.compose.ui.text.input.VisualTransformation


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isLoggedIn: Boolean by sharedViewModel.isLoggedIn.observeAsState(false)
    val userRole by sharedViewModel.userRole.observeAsState()
    val loading: Boolean by sharedViewModel.loading.observeAsState(false)

    // Observe loading state
    val isLoading by sharedViewModel.loading.observeAsState(initial = false)


    if (isLoggedIn) {
        // Navigate based on user role
        LaunchedEffect(userRole) { // Use LaunchedEffect to prevent multiple navigations
            when (userRole) {
                "ADMIN" -> navController.navigate(Screen.AdminHome.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
                "USER" -> navController.navigate(Screen.UserHome.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
                else -> { /* Handle unknown role or error */ }
            }
        }
    }

    // UI for the login screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
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
        Spacer(modifier = Modifier.height(16.dp))

        // Show loading indicator when loading
        if (isLoading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(8.dp))
        }


        Button(
            onClick = remember { {
                sharedViewModel.login(username, password)
            } },
            enabled = !loading, // Disable the button when loading
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Navigate to Registration Scree
        TextButton(onClick = { navController.navigate(Screen.Register.route) }) {
            Text("Don't have an account? Register here")
        }
    }
}