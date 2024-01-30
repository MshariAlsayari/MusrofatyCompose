package com.msharialsayari.musrofaty.base

import android.app.Application
import com.google.gson.Gson
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.ApiResponse
import com.msharialsayari.musrofaty.business_layer.ErrorResponse
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

abstract class BaseNetworkResponse {

    @Inject
    lateinit var appContext: Application
    private lateinit var defaultErrorMessage :String
    private lateinit var noInternetConnectionErrorMessage :String


    protected suspend fun <T : Any> getApiResponse(call: suspend () -> Response<T> ): ApiResponse<T> {
        fillErrorMessage()
        return try {
            val response = call()
            if (response.isSuccessful) {
                ApiResponse.Success(response.body())
            } else {
                handleErrorResult(response)
            }
        } catch (e: Exception) {
            handleExceptionErrorResult(e)
        }
        
    }


    private fun <T : Any> handleErrorResult(response: Response<T>): ApiResponse<T> {

//        if (response.code() == 401) {
//            if (securePreferences.isLoggedIn()) {
//              logoutManager.logout()
//            }
//        }
        return try {
            val errorModel = Gson().fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
            val errorMessage: String = errorModel.errorMessage?: defaultErrorMessage
            getError(message = errorMessage, code =  response.code())
        } catch (e: RuntimeException) {
            getError(defaultErrorMessage, response.code())
        }
    }



    private fun <T : Any> handleExceptionErrorResult(e: Exception): ApiResponse<T> {
        return when (e) {
            is UnknownHostException -> {
                 getError(noInternetConnectionErrorMessage)
            }

            is ConnectException ->{
                getError(noInternetConnectionErrorMessage)
            }

            else -> {
                getError(defaultErrorMessage)
            }
        }
    }

    private fun <T: Any> getError(message: String, code: Int = 0): ApiResponse<T> {
        return ApiResponse.Failure(ErrorResponse(errorMessage = message, errorCode = code))
    }

    private fun fillErrorMessage(){
        defaultErrorMessage = appContext.getString(R.string.common_network_error_message)
        noInternetConnectionErrorMessage = appContext.getString(R.string.common_network_no_connection_message)
    }

}