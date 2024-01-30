package com.msharialsayari.musrofaty.business_layer.data_layer.categories

import retrofit2.Response
import retrofit2.http.GET

interface CategoryApiService {

    @GET("musrofaty-app/api/v1/categories")
    suspend fun getCategory(): Response<CategoryContainer>
}