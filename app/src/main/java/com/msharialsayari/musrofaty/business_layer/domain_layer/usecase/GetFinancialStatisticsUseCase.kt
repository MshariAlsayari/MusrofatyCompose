package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.StatisticsRepo
import com.msharialsayari.musrofaty.utils.models.FinancialStatistics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetFinancialStatisticsUseCase @Inject constructor(
    private val statisticsRepo: StatisticsRepo
) {

    operator fun invoke(list:List<SmsModel>): Map<String, FinancialStatistics> {
        return statisticsRepo.getFinancialStatistics(list)

    }
}