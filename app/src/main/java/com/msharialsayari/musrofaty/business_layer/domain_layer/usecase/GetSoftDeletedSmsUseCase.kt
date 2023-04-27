package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import androidx.paging.PagingData
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import com.msharialsayari.musrofaty.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetSoftDeletedSmsUseCase @Inject constructor(
    private val smsRepo: SmsRepo
) {

    operator fun invoke(senderId: Int,filterOption: DateUtils.FilterOption = DateUtils.FilterOption.ALL, query:String="", startDate:Long = 0, endDate:Long= 0): Flow<PagingData<SmsEntity>> {
        return smsRepo.getAllSoftDeletedSms(senderId,filterOption,query,startDate, endDate)

    }
}