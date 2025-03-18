// client/app/src/main/java/com/group2/client/ui/viewmodel/UserViewModel.kt
package com.group2.client.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group2.client.api.RetrofitClient
import com.group2.client.model.AdoptionRequest
import com.group2.client.model.Dog
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val _availableDogs = MutableLiveData<List<Dog>>()
    val availableDogs: LiveData<List<Dog>> = _availableDogs

    private val _userAdoptionRequests = MutableLiveData<List<AdoptionRequest>>() // User's requests
    val userAdoptionRequests: LiveData<List<AdoptionRequest>> = _userAdoptionRequests

    private val _error = MutableLiveData<String?>() // Use nullable String
    val error: LiveData<String?> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _userId = MutableLiveData<Int?>() // Change to Int?
    val userId: LiveData<Int?> = _userId

    fun setUserId(id: Int?) { // Change to Int?
        _userId.value = id
    }

    init{
        getAvailableDogs()
    }

    fun getAvailableDogs() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = RetrofitClient.instance.getAvailableDogs()
                if (response.isSuccessful) {
                    _availableDogs.value = response.body()?.data ?: emptyList()
                } else {
                    _error.value = response.body()?.message ?: "Error fetching dogs: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.localizedMessage}"
            } finally {
                _loading.value = false
            }
        }
    }
    fun getAdoptionRequestsByUserId(userId: Int) { // Change to Int
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = RetrofitClient.instance.getAdoptionRequestsByUserId(userId)
                if (response.isSuccessful) {
                    _userAdoptionRequests.value = response.body()?.data ?: emptyList()
                } else {
                    _error.value = response.body()?.message ?: "Error fetching requests: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.localizedMessage}"
            } finally {
                _loading.value = false
            }
        }
    }
    fun createAdoptionRequest(request: AdoptionRequest) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = RetrofitClient.instance.createAdoptionRequest(request)
                if (response.isSuccessful) {
                    userId.value?.let { getAdoptionRequestsByUserId(it) }
                } else {
                    _error.value = response.body()?.message ?: "Error creating request: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.localizedMessage}"
            } finally {
                _loading.value = false
            }
        }
    }
}