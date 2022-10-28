package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryStatistics
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.StatisticsRepo
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetCategoriesStatisticsUseCase @Inject constructor(
    private val statisticsRepo: StatisticsRepo
) {

    suspend operator fun invoke(list:List<SmsEntity>): Map<Int, CategoryStatistics> {
        return statisticsRepo.getCategorySummaryOfSmsList(list)

    }
}