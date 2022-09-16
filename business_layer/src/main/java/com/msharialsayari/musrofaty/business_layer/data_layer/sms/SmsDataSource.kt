package com.msharialsayari.musrofaty.business_layer.data_layer.sms

import android.content.Context
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.WordDetectorModel

interface SmsDataSource {
    suspend fun loadBanksSms(context: Context): List<SmsModel>
}