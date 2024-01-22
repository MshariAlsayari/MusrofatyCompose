package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import androidx.paging.PagingData
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import com.msharialsayari.musrofaty.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ObservingPaginationAllSmsUseCase @Inject constructor(
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
    ): Flow<PagingData<SmsModel>> {
        return smsRepo.observingPaginationAllSms(senderId, filterOption, isDeleted,isFavorite, query, startDate, endDate)
    }
}