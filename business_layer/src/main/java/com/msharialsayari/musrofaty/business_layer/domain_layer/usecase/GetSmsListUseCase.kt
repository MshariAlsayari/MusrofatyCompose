package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import com.msharialsayari.musrofaty.utils.DateUtils
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetSmsListUseCase @Inject constructor(
    private val smsRepo: SmsRepo
) {

    suspend operator fun invoke(senderId: Int, filterOption: DateUtils.FilterOption = DateUtils.FilterOption.ALL, query:String="", startDate:Long = 0, endDate:Long= 0): List<SmsModel> {
        return smsRepo.getAll(senderId,filterOption,query,startDate, endDate)
    }
}