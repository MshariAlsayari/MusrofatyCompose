package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.FilterRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddFilterWordUseCase @Inject constructor(
    private val filterRepo: FilterRepo
) {

    suspend operator fun invoke(filterId:Int, word:String) {
         filterRepo.addFilterWord(filterId, word)
    }


}