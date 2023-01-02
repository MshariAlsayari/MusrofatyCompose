package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreWithCategory
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.StoreRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetAllStoreWithCategoryUseCase @Inject constructor(
    private val storeRepo: StoreRepo) {

    operator fun  invoke(query:String = ""): Flow<List<StoreWithCategory>> {
        return storeRepo.getAll(query)
    }
}