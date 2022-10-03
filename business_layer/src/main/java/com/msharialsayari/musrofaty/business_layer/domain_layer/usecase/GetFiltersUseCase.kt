package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterAdvancedModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.FilterRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetFiltersUseCase @Inject constructor(
    private val filterRepo: FilterRepo
) {

    suspend operator fun invoke(senderName: String, senderId:Int): List<FilterAdvancedModel> {
        return filterRepo.getAll(senderName,senderId)

    }


}