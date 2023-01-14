package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SenderRepo
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.StoreFirebaseRepo
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.StoreRepo
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PostStoreToFirestoreUseCase @Inject constructor(
    private val storeRepo: StoreFirebaseRepo,
    private val getCategoryUseCase: GetCategoryUseCase
) {

    suspend operator fun invoke(storeEntity: StoreEntity) {
        val category = getCategoryUseCase(storeEntity.category_id)

        if (category?.isDefault ==true)
           storeRepo.postStoreToFirestare(storeEntity)
    }
}