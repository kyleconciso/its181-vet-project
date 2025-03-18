// client/app/src/main/java/com/group2/client/ui/navigation/NavGraph.kt (Modified)
package com.group2.client.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.group2.client.ui.screens.LoginScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.group2.client.ui.viewmodel.SharedViewModel // Import SharedViewModel
import com.group2.client.ui.screens.admin.AdminHomeScreen
import com.group2.client.ui.viewmodel.AdminViewModel
import com.group2.client.ui.screens.user.UserHomeScreen
import com.group2.client.ui.viewmodel.UserViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.group2.client.ui.screens.user.DogDetailScreenUser
import com.group2.client.ui.screens.admin.AddEditDogScreen
import com.group2.client.ui.screens.admin.DogDetailScreenAdmin
import com.group2.client.ui.screens.RegisterScreen
import com.group2.client.ui.screens.admin.AdminRequestsScreen // Import
import com.group2.client.ui.screens.user.UserRequestsScreen


@Composable
fun NavGraph(
    sharedViewModel: SharedViewModel = viewModel(),
    adminViewModel: AdminViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
) {
    val navController = rememberNavController()
    val isLoggedIn: Boolean by sharedViewModel.isLoggedIn.observeAsState(false)
    val userRole by sharedViewModel.userRole.observeAsState()

    // Pass the UserViewModel instance to the SharedViewModel
    sharedViewModel.setUserViewModel(userViewModel)

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            if (isLoggedIn) {
                if (userRole == "ADMIN") {
                    navController.navigate(Screen.AdminHome.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                } else {
                    navController.navigate(Screen.UserHome.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            } else {
                LoginScreen(navController = navController, sharedViewModel = sharedViewModel)
            }
        }
        composable(Screen.UserHome.route) {
            UserHomeScreen(navController = navController, userViewModel = userViewModel, sharedViewModel = sharedViewModel)
        }
        composable(
            route = Screen.DogDetailUser.route,
            arguments = listOf(navArgument("dogId") { type = NavType.LongType })
        ) { backStackEntry ->
            val dogId = backStackEntry.arguments?.getLong("dogId") ?: -1L
            DogDetailScreenUser(dogId = dogId, navController = navController, userViewModel = userViewModel)
        }
        composable(Screen.AdminHome.route) {
            AdminHomeScreen(navController = navController, adminViewModel = adminViewModel, sharedViewModel = sharedViewModel)
        }
        composable(
            route = Screen.AddEditDog.route,
            arguments = listOf(navArgument("dogId") { type = NavType.LongType })
        ) { backStackEntry ->
            val dogId = backStackEntry.arguments?.getLong("dogId") ?: -1L
            AddEditDogScreen(dogId = dogId, navController = navController, adminViewModel = adminViewModel)
        }
        composable(
            route = Screen.DogDetailAdmin.route,
            arguments = listOf(navArgument("dogId") { type = NavType.LongType })
        ) {backStackEntry ->
            val dogId = backStackEntry.arguments?.getLong("dogId") ?: -1L
            DogDetailScreenAdmin(dogId = dogId, navController = navController, adminViewModel = adminViewModel)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
        composable(Screen.AdminRequests.route) {
            AdminRequestsScreen(adminViewModel = adminViewModel)
        }
        composable(Screen.UserRequests.route) { // NEW: Route for user requests
            UserRequestsScreen(userViewModel = userViewModel)
        }
    }
}