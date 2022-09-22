package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SenderRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateSenderCategoryUseCase@Inject constructor(
    private val senderRepo: SenderRepo
) {

    suspend operator fun invoke(senderId:Int, contentCategoryId:Int,) {
        senderRepo.changeCategory(senderId,contentCategoryId)

    }
}