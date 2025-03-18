// client/app/src/main/java/com/group2/client/api/DogAdoptionApi.kt
package com.group2.client.api

import com.group2.client.model.AdoptionRequest
import com.group2.client.model.Dog
import com.group2.client.model.StatusUpdateRequest // Import
import retrofit2.Response
import retrofit2.http.*
import com.group2.client.model.ApiResponse

interface DogAdoptionApi {

    @GET("/api/dogs")
    suspend fun getAllDogs(): Response<ApiResponse<List<Dog>>>

    @GET("/api/dogs/available")
    suspend fun getAvailableDogs(): Response<ApiResponse<List<Dog>>>

    @GET("/api/dogs/{id}")
    suspend fun getDogById(@Path("id") dogId: Long): Response<ApiResponse<Dog>>

    @POST("/api/dogs")
    suspend fun createDog(@Body dog: Dog): Response<ApiResponse<Dog>>

    @PUT("/api/dogs/{id}")
    suspend fun updateDog(@Path("id") dogId: Long, @Body dog: Dog): Response<ApiResponse<Dog>>

    @DELETE("/api/dogs/{id}")
    suspend fun deleteDog(@Path("id") dogId: Long): Response<ApiResponse<Unit>>

    @POST("/api/requests")
    suspend fun createAdoptionRequest(@Body request: AdoptionRequest): Response<ApiResponse<AdoptionRequest>>

    @GET("/api/requests/all")
    suspend fun getAllAdoptionRequests(): Response<ApiResponse<List<AdoptionRequest>>>

    @GET("/api/requests/user/{userId}")
    suspend fun getAdoptionRequestsByUserId(@Path("userId") userId: Int): Response<ApiResponse<List<AdoptionRequest>>>

    @GET("/api/requests/{id}")
    suspend fun getAdoptionRequestById(@Path("id") requestId: Int): Response<ApiResponse<AdoptionRequest>>

    @PUT("/api/requests/{id}/status")
    suspend fun updateAdoptionRequestStatus(
        @Path("id") requestId: Int,
        @Body statusUpdate: StatusUpdateRequest // Use StatusUpdateRequest
    ): Response<ApiResponse<AdoptionRequest>>

    @POST("/api/auth/login")
    suspend fun login(@Body credentials: Map<String, String>): Response<ApiResponse<Map<String, Any>>>

    @POST("/api/auth/register")
    suspend fun register(@Body userData: Map<String, String>): Response<ApiResponse<Void>>
}