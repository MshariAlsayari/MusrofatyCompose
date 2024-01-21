package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.base.Response
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.StoresCategoriesModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.StoreFirebaseRepo
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.StoresCategoriesFirebaseRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetStoresCategoriesKeysUseCase @Inject constructor(
    private val repo: StoresCategoriesFirebaseRepo,
) {

    suspend operator fun invoke(): Flow<Response<List<StoresCategoriesModel>>> {
        return repo.getStoresAndCategoriesFromFirebase()
    }
}