// client/app/src/main/java/com/group2/client/model/StatusUpdateRequest.kt
package com.group2.client.model

import com.google.gson.annotations.SerializedName

data class StatusUpdateRequest(
    @SerializedName("status") val status: String
)