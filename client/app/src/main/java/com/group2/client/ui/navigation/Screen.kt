// client/app/src/main/java/com/group2/client/ui/navigation/Screen.kt (Modified)
package com.group2.client.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object UserHome : Screen("user_home")
    object DogDetailUser : Screen("dog_detail_user/{dogId}") {
        fun createRoute(dogId: Long) = "dog_detail_user/$dogId"
    }
    object AdminHome : Screen("admin_home")
    object AddEditDog : Screen("add_edit_dog/{dogId}"){
        fun createRoute(dogId: Long) = "add_edit_dog/$dogId"
    }
    object DogDetailAdmin : Screen("dog_detail_admin/{dogId}"){
        fun createRoute(dogId: Long) = "dog_detail_admin/$dogId"
    }

    object Register : Screen("register") // Add a new screen for registration
    object AdminRequests : Screen("admin_requests") // Add the new screen
    object UserRequests : Screen("user_requests") // NEW: User requests screen
}