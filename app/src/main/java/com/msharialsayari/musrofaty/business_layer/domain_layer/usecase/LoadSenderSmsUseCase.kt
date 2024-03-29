package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LoadSenderSmsUseCase @Inject constructor(
    private val smsRepo: SmsRepo
) {

    suspend operator fun invoke(senderName:String) {
        smsRepo.insertSenderSMSs(senderName)

    }
}