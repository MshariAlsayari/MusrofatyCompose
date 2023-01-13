package com.msharialsayari.musrofaty.base

sealed class Response<out T> {
    class Loading<out T>: Response<T>()

    data class Success<out T>(
        val data: T
    ): Response<T>()

    data class Failure<out T>(
        val errorMessage: String
    ): Response<T>()
}