package com.group2.client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.group2.client.ui.navigation.NavGraph
import com.group2.client.ui.theme.ClientTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.group2.client.ui.viewmodel.SharedViewModel // Import SharedViewModel
import com.group2.client.ui.viewmodel.AdminViewModel
import com.group2.client.ui.viewmodel.UserViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClientTheme {
                // Instantiate the SharedViewModel using viewModel()
                val sharedViewModel: SharedViewModel = viewModel()
                val adminViewModel: AdminViewModel = viewModel()
                val userViewModel: UserViewModel = viewModel()
                NavGraph(sharedViewModel = sharedViewModel, adminViewModel, userViewModel)
            }
        }
    }
}