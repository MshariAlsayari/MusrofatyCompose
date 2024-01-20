package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoriesChartModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.StatisticsRepo
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetCategoriesStatisticsChartUseCase @Inject constructor(
    private val statisticsRepo: StatisticsRepo
) {

     operator fun invoke(key: String, list: List<SmsModel>): CategoriesChartModel {
        return statisticsRepo.getCategoriesStatisticsChartData(key, list)

    }
}