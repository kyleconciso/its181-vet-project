//AdoptionRequest model
package com.group2.client.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class AdoptionRequest(
    @SerializedName("id") val id: Int? = null, // ID can be null when creating a new request, Change to Int
    @SerializedName("dogId") val dogId: Int, // Change to Int
    @SerializedName("userId") val userId: Int, // Change to Int
    @SerializedName("requestDate") val requestDate: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("userEmail") val userEmail: String,
)