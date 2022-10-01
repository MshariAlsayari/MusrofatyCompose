package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import androidx.paging.PagingData
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetFavoriteSmsUseCase  @Inject constructor(
    private val smsRepo: SmsRepo
) {

    operator fun invoke(senderId: Int, isFavorite:Boolean = true): Flow<PagingData<SmsEntity>> {
        return smsRepo.getAllFavoriteSms(senderId,isFavorite)

    }
}