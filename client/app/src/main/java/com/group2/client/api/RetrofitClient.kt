// client/app/src/main/java/com/group2/client/api/RetrofitClient.kt
package com.group2.client.api

import com.group2.client.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.Credentials // Import Credentials

object RetrofitClient {

    private var username = ""
    private var password = ""
    // No token needed for basic auth

    fun setCredentials(user: String, pass: String) {
        username = user
        password = pass
        // No need to reset a token
    }

    fun clearCredentials() {
        username = ""
        password = ""
        // No token to clear
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()

            val requestBuilder = original.newBuilder().apply {
                // Always use Basic Auth if credentials are set
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    header("Authorization", Credentials.basic(username, password))
                }
            }
                .method(original.method, original.body)

            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Add logging interceptor
        })
        .build()

    val instance: DogAdoptionApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(DogAdoptionApi::class.java)
    }
}