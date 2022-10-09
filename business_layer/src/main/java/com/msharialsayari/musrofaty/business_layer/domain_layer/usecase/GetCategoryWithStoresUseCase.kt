package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryWithStore
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.CategoryRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetCategoryWithStoresUseCase @Inject constructor(
    private val categoryRepo: CategoryRepo
) {

     operator fun invoke(categoryId: Int): Flow<CategoryWithStore>? {
        return categoryRepo.getCategoryAndStores(categoryId)

    }
}