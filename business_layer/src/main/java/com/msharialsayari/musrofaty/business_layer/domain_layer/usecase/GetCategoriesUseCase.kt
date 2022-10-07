package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.CategoryRepo
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetCategoriesUseCase @Inject constructor(
    private val categoryRepo: CategoryRepo
) {

     operator fun invoke(): Flow<List<CategoryEntity>> {
        return categoryRepo.getAll()
    }
}