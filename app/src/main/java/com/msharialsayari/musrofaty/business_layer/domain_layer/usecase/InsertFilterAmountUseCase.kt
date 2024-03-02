package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterAmountModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterWordModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.FilterRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsertFilterAmountUseCase @Inject constructor(
    private val filterRepo: FilterRepo
) {
    suspend operator fun invoke(filter: FilterAmountModel){
        return filterRepo.saveFilterAmount(filter)
    }
}