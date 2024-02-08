package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.FilterRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetFiltersUseCase @Inject constructor(
    private val filterRepo: FilterRepo
) {

     suspend operator fun invoke(senderId:Int):List<FilterModel> {
        return filterRepo.getAll(senderId)

    }


}