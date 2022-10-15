package com.msharialsayari.musrofaty

import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.utils.DateUtils

object Utils {

    @JvmStatic
    fun getMappedSmsByMonth(
        smsList: List<SmsModel>,
        pattern: String
    ): Map<String, List<SmsModel>> {
        return smsList.groupBy {
         DateUtils.getDateByTimestamp(it.timestamp, pattern)!!
        }
    }
}