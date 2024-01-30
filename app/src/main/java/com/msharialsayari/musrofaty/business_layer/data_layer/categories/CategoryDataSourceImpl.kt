package com.msharialsayari.musrofaty.business_layer.data_layer.categories

import com.msharialsayari.musrofaty.base.BaseNetworkResponse
import com.msharialsayari.musrofaty.business_layer.ApiResponse
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CategoryDataSourceImpl@Inject constructor(
    private val categoryApiService: CategoryApiService,
) : CategoryDataSource , BaseNetworkResponse(){


    override suspend fun getCategories(): ApiResponse<CategoryContainer> {
        return getApiResponse { categoryApiService.getCategory() }
    }
}