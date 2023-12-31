package com.msharialsayari.musrofaty.business_layer.data_layer.sms

import android.content.Context
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity

interface SmsDataSource {
    suspend fun loadBanksSms(context: Context): List<SmsEntity>
    suspend fun loadBanksSms(context: Context, senderName:String): List<SmsEntity>
}