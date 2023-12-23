package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import com.msharialsayari.musrofaty.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ObservingAllSmsUseCase @Inject constructor(
    private val smsRepo: SmsRepo
) {

    operator fun invoke(
        senderId: Int?= null,
        filterOption: DateUtils.FilterOption = DateUtils.FilterOption.ALL,
        isDeleted: Boolean?= null,
        isFavorite:Boolean?=null,
        query: String = "",
        startDate: Long = 0,
        endDate: Long = 0
    ): Flow<List<SmsEntity>> {
        return smsRepo.observingAllSms(senderId, filterOption, isDeleted,isFavorite, query, startDate, endDate)
    }
}