// client/app/src/main/java/com/group2/client/model/Dog.kt
package com.group2.client.model

import com.google.gson.annotations.SerializedName

data class Dog(
    @SerializedName("id") val id: Int? = null, // Make id nullable
    @SerializedName("name") val name: String,
    @SerializedName("breed") val breed: String,
    @SerializedName("age") val age: Int,
    @SerializedName("gender") val gender: String,
    @SerializedName("description") val description: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("available") val isAvailable: Boolean
)