package com.msharialsayari.musrofaty.business_layer.data_layer.categories

import com.msharialsayari.musrofaty.business_layer.ApiResponse


interface CategoryDataSource {
    suspend fun getCategories(): ApiResponse<CategoryContainer>
}