package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SenderRepo
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetSendersUseCase @Inject constructor(
    private val senderRepo: SenderRepo) {

    suspend operator fun invoke(): List<SenderModel> {
        val result = senderRepo.getAllActive()
        result.sortedBy { !it.isPined }
        return result

    }
}