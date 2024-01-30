package com.msharialsayari.musrofaty.business_layer

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("code")
    val errorCode: Int? = null,
    @SerializedName("message")
    val errorMessage: String? = null,
)