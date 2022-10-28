package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import com.msharialsayari.musrofaty.utils.DateUtils
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetAllSmsForSendersUseCase @Inject constructor(
    private val smsRepo: SmsRepo
) {

     suspend operator fun invoke(filterOption: DateUtils.FilterOption = DateUtils.FilterOption.ALL, query:String="", startDate:Long = 0, endDate:Long= 0 ): List<SmsEntity> {
         return smsRepo.getAllSmsForAllSenders(filterOption, query, startDate, endDate)

    }
}