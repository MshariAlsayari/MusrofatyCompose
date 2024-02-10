package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterWordModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.FilterRepo
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class InsertFilterUseCase @Inject constructor(
    private val filterRepo: FilterRepo
) {

    suspend operator fun invoke(filter: FilterModel): List<Long> {
        return filterRepo.saveFilter(filter)
    }


}