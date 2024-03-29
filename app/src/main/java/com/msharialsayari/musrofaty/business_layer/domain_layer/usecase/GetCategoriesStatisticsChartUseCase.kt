package com.msharialsayari.musrofaty.business_layer.domain_layer.usecase

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoriesChartModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.StatisticsRepo
import com.msharialsayari.musrofaty.utils.DateUtils
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetCategoriesStatisticsChartUseCase @Inject constructor(
    private val statisticsRepo: StatisticsRepo
) {

    operator fun invoke(
        key: String,
        filterOption: DateUtils.FilterOption,
        list: List<SmsModel>
    ): CategoriesChartModel {
        return statisticsRepo.getCategoriesStatisticsChartData(key, filterOption,list)

    }
}