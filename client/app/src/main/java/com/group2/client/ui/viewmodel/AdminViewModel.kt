// client/app/src/main/java/com/group2/client/ui/viewmodel/AdminViewModel.kt
package com.group2.client.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group2.client.api.RetrofitClient
import com.group2.client.model.AdoptionRequest
import com.group2.client.model.Dog
import com.group2.client.model.StatusUpdateRequest // Import
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {

    private val _dogs = MutableLiveData<List<Dog>>()
    val dogs: LiveData<List<Dog>> = _dogs

    private val _requests = MutableLiveData<List<AdoptionRequest>>()
    val requests: LiveData<List<AdoptionRequest>> = _requests

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    init {
        getAllDogs()
        getAllAdoptionRequests()
    }

    fun getAllDogs() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = RetrofitClient.instance.getAllDogs()
                if (response.isSuccessful) {
                    _dogs.value = response.body()?.data ?: emptyList()
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

    fun getAllAdoptionRequests() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = RetrofitClient.instance.getAllAdoptionRequests()
                if (response.isSuccessful) {
                    _requests.value = response.body()?.data ?: emptyList()
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

    fun createDog(dog: Dog) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = RetrofitClient.instance.createDog(dog)
                if (response.isSuccessful) {
                    getAllDogs()
                } else {
                    _error.value = response.body()?.message ?: "Error creating dog: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateDog(dog: Dog) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                dog.id?.let {
                    val response = RetrofitClient.instance.updateDog(it.toLong(), dog)
                    if (response.isSuccessful) {
                        getAllDogs()
                    } else {
                        _error.value = response.body()?.message ?: "Error updating dog: ${response.code()}"
                    }
                } ?: run {
                    _error.value = "Dog ID is null. Cannot update."
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun deleteDog(dogId: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = RetrofitClient.instance.deleteDog(dogId.toLong())
                if (response.isSuccessful) {
                    getAllDogs()
                } else {
                    _error.value = response.body()?.message ?: "Error deleting dog: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateRequestStatus(requestId: Int, newStatus: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val request = StatusUpdateRequest(newStatus) // Create the request object
                val response = RetrofitClient.instance.updateAdoptionRequestStatus(requestId, request) // Pass object
                if (response.isSuccessful) {
                    getAllAdoptionRequests()
                } else {
                    _error.value = response.body()?.message ?: "Error updating request: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}