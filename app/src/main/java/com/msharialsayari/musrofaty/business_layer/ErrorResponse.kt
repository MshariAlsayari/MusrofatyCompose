package com.msharialsayari.musrofaty.business_layer

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("status")
    val errorCode: Int? = null,
    @SerializedName("title")
    val errorMessage: String? = null,
)