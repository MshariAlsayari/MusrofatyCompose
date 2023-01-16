package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SenderRepo
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AddSenderUseCase @Inject constructor(
    private val senderRepo: SenderRepo,
    private val smsRepo: SmsRepo
) {

    suspend operator fun invoke(senderModel: SenderModel) {
        senderModel.senderName = senderModel.senderName.trim()
        senderRepo.insert(senderModel)
        smsRepo.insert(senderModel.senderName)

    }
}