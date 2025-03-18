package com.group2.client.model

data class ApiResponse<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T? = null
)