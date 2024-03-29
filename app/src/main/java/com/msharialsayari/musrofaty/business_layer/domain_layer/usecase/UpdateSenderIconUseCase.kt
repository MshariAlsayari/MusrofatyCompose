package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SenderRepo
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UpdateSenderIconUseCase @Inject constructor(
    private val senderRepo: SenderRepo
) {

    suspend operator fun invoke(senderId:Int, iconPath:String) {
        senderRepo.changeSenderIcon(senderId,iconPath)

    }
}