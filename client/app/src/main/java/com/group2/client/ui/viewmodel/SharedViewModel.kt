// client/app/src/main/java/com/group2/client/ui/viewmodel/SharedViewModel.kt
package com.group2.client.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group2.client.api.RetrofitClient
import kotlinx.coroutines.launch
import com.group2.client.model.ApiResponse // Import

class SharedViewModel : ViewModel() {
    private val _isLoggedIn = MutableLiveData(false)
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    private val _userRole = MutableLiveData<String?>(null)
    val userRole: LiveData<String?> = _userRole

    private val _username = MutableLiveData<String?>(null) // Add username
    val username: LiveData<String?> = _username


    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    // Error handling for registration
    private val _registrationError = MutableLiveData<String?>()
    val registrationError: LiveData<String?> = _registrationError

    private val _loginError = MutableLiveData<String?>() // Login-specific error
    val loginError: LiveData<String?> = _loginError


    fun login(username: String,password: String) {
        viewModelScope.launch {
            _loading.value = true
            _loginError.value = null // Clear previous login errors
            try {
                // Pass credentials as a Map
                val response = RetrofitClient.instance.login(mapOf("username" to username, "password" to password))
                if (response.isSuccessful) {
                    val responseBody = response.body()?.data
                    if(responseBody != null){
                        _isLoggedIn.value = true
                        _userRole.value = responseBody["role"] as? String ?: "USER"
                        val userId = (responseBody["userId"] as? Number)?.toInt()  //Get Int
                        if( _userRole.value == "USER") {
                            userViewModel.setUserId(userId) // Now correctly passing Int?
                            if (userId != null) {
                                userViewModel.getAdoptionRequestsByUserId(userId) // Correctly passing Int
                            }
                        }
                        _username.value = responseBody["username"] as? String
                        RetrofitClient.setCredentials(username, password)
                    } else {
                        _loginError.value = response.body()?.message ?: "Login failed: ${response.code()}"
                        _isLoggedIn.value = false
                    }

                } else {
                    // Handle unsuccessful login
                    _loginError.value = response.body()?.message ?: "Login failed: ${response.code()}" // Set login error
                    _isLoggedIn.value = false
                    _userRole.value = null
                    _username.value = null;
                }
            } catch (e: Exception) {
                // _error.value = "Network error: ${e.localizedMessage}"
                _loginError.value = "Network error: ${e.message}" // Set login error
                _isLoggedIn.value = false // Ensure user is logged out
            } finally {
                _loading.value = false
            }
        }
    }
    fun logout() {
        _isLoggedIn.value = false
        _userRole.value = null
        _username.value = null
        userViewModel.setUserId(null) // Clear user ID
        RetrofitClient.clearCredentials() // Clear stored credentials
    }


    // Setter for UserViewModel instance
    fun setUserViewModel(viewModel: UserViewModel) {
        userViewModel = viewModel
    }

    // Keep a reference to the UserViewModel
    private lateinit var userViewModel: UserViewModel

    fun register(userData: Map<String, String>) {
        viewModelScope.launch {
            _loading.value = true
            _registrationError.value = null // Clear previous errors
            try {
                val response = RetrofitClient.instance.register(userData)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.success) {
                        // Registration successful, you might navigate to login or show a success message
                        println("Registration Success: ${apiResponse.message}")
                        _registrationError.value = "Registration successful!  Please log in." // Or navigate
                    } else {
                        // Registration failed, handle the server's error message
                        val errorMessage = apiResponse?.message ?: "Unknown error"
                        println("Registration Failed: $errorMessage")
                        _registrationError.value = errorMessage
                    }
                } else {
                    // Handle non-successful HTTP responses (e.g., 400, 500)
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    println("Registration Failed (HTTP): $errorMessage")
                    _registrationError.value = errorMessage
                }
            } catch (e: Exception) {
                // Handle network errors
                println("Registration Network Error: ${e.message}")
                _registrationError.value = "Network error: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}