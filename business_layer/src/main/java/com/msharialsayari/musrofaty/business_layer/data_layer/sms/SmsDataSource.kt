package com.msharialsayari.musrofaty.business_layer.data_layer.sms

import android.content.Context
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel

interface SmsDataSource {
    suspend fun loadBanksSms(context: Context): List<SmsModel>
}