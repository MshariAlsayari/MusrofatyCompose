package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.CategoryRepo
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AddCategoryUseCase  @Inject constructor(
    private val categoryRepo: CategoryRepo
) {

    suspend operator fun invoke(categoryModel: CategoryModel) {
        categoryRepo.insert(categoryModel)
    }


}