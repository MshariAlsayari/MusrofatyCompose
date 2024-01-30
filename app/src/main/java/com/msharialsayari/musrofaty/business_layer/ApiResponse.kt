package com.msharialsayari.musrofaty.business_layer

sealed class ApiResponse <out T: Any>{
    data class Success<T: Any>( val data: T?): ApiResponse<T>()
    data class Failure(val errorResponse: ErrorResponse): ApiResponse<Nothing>()
}
