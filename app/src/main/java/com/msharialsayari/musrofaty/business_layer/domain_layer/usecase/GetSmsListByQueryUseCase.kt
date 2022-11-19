package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import androidx.paging.PagingData
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import com.msharialsayari.musrofaty.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetSmsListByQueryUseCase @Inject constructor(
    private val smsRepo: SmsRepo
) {

    operator fun invoke( query:String=""): Flow<PagingData<SmsEntity>> {
        return smsRepo.getAllSms(query)
    }
}