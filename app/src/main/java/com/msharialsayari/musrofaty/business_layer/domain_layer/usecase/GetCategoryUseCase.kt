package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.CategoryRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCategoryUseCase @Inject constructor(
    private val categoryRepo: CategoryRepo
) {

    suspend operator fun invoke(categoryId: Int): CategoryModel? {
        return categoryRepo.getCategory(categoryId)

    }
}