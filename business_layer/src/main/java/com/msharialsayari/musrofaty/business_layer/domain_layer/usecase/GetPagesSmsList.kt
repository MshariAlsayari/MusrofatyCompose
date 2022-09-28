package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import androidx.paging.PagingData
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetPagesSmsList @Inject constructor(
    private val smsRepo: SmsRepo
) {

    suspend operator fun invoke(senderId:Int): Flow<PagingData<SmsModel>> {
        val result = smsRepo.getPagesSmsList(senderId)
        return result

    }
}