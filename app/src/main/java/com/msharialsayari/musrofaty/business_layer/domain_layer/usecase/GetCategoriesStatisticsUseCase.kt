package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryContainerStatistics
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.StatisticsRepo
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetCategoriesStatisticsUseCase @Inject constructor(
    private val statisticsRepo: StatisticsRepo
) {

    operator fun invoke(key:String, list:List<SmsModel>): CategoryContainerStatistics {
        return statisticsRepo.getCategorySummaryOfSmsList(key, list)

    }
}