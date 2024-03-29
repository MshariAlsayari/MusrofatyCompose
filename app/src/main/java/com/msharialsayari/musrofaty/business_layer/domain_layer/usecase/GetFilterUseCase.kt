package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterAdvancedModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterWithWordsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.FilterRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetFilterUseCase @Inject constructor(
    private val filterRepo: FilterRepo
) {

    suspend operator fun invoke(filterId:Int):FilterWithWordsModel? {
        return filterRepo.getFilter(filterId)
    }


}