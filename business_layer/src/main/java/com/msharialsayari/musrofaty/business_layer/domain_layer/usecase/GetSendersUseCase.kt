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
        val result = senderRepo.getAllActive()
        val map = mutableMapOf<SenderModel, List<SmsModel>>()
        result.groupBy { it }.entries.map {
            map.putIfAbsent(it.key, emptyList<SmsModel>())
        }

        map.entries.sortedWith(compareBy { !it.key.isPined })
        return map

    }
}