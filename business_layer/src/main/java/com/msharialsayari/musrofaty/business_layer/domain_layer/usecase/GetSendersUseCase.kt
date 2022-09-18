package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.ContentRepo
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SenderRepo
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetSendersUseCase @Inject constructor(
    private val senderRepo: SenderRepo,
    private val smsRepo: SmsRepo,
    private val contentRepo: ContentRepo) {

    suspend operator fun invoke(): Map<SenderModel, List<SmsModel>> {
        val result = senderRepo.getAllSendersWithSms()
        result.map {
            it.sender.content = contentRepo.getContentById(it.sender.contentId)
            it.sms = smsRepo.getSmsBySenderName(it.sender.senderName)
        }
        val map = mutableMapOf<SenderModel, List<SmsModel>>()
        result.groupBy { it.sender }.entries.map {
            map.putIfAbsent(it.key, it.value.flatMap { it.sms })
        }

        map.entries.sortedWith(compareBy { !it.key.isPined })
        return map

    }
}