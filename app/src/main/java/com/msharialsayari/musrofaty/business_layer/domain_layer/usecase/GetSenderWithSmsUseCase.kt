package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database.SenderWithRelationsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SenderRepo
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetSenderWithSmsUseCase @Inject constructor(
    private val senderRepo: SenderRepo
)  {

    suspend operator fun invoke(senderId:Int): SenderWithRelationsModel {
        return senderRepo.getSenderByIdWithSms(senderId)

    }
}