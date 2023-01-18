package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import com.msharialsayari.musrofaty.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSmsBySenderIdWithDeleteCheckUseCase @Inject constructor(
    private val smsRepo: SmsRepo
) {

    operator fun invoke(senderId: Int,isDeleted:Boolean,filterOption: DateUtils.FilterOption = DateUtils.FilterOption.ALL, query:String="", startDate:Long = 0, endDate:Long= 0): Flow<List<SmsEntity>> {
        return smsRepo.getSmsBySenderIdWithSoftDeletedCheck(senderId,isDeleted,filterOption,query,startDate, endDate)
    }
}