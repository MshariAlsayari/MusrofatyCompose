package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterWithWordsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterWordModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.FilterRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class InsertFilterWordUseCase @Inject constructor(
    private val filterRepo: FilterRepo
) {

     suspend operator fun invoke(filter:FilterWordModel){
        return filterRepo.saveFilterWords(filter)
    }


}