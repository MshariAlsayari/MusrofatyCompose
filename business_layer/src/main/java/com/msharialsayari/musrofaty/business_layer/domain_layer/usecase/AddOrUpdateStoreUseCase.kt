package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreAndCategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.StoreModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.StoreRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddOrUpdateStoreUseCase @Inject constructor(
    private val storeRepo: StoreRepo
) {

    suspend operator fun invoke(storeModel: StoreModel) {
         storeRepo.insertOrUpdateIfExisted(storeModel)
    }
}