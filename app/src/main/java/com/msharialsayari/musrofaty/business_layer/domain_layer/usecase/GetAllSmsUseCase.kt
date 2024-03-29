package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterAmountModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import com.msharialsayari.musrofaty.utils.DateUtils
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetAllSmsUseCase @Inject constructor(private val smsRepo: SmsRepo) {

    suspend operator fun invoke(
        senderId: Int? = null,
        filterOption: DateUtils.FilterOption = DateUtils.FilterOption.ALL,
        isDeleted: Boolean? = null,
        isFavorite: Boolean? = null,
        isFilter: Boolean = false,
        filterAmountModel: FilterAmountModel? = null,
        query: String = "",
        startDate: Long = 0,
        endDate: Long = 0,
    ): List<SmsModel> {
        return smsRepo.getAllSms(
            senderId,
            filterOption,
            isDeleted,
            isFavorite,
            isFilter,
            filterAmountModel,
            query,
            startDate,
            endDate
        )
    }
}