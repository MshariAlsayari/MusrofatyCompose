package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import android.content.Context
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.toSmsModel
import com.msharialsayari.musrofaty.business_layer.data_layer.sms.SmsDataSource
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.WordDetectorType
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toSmsEntity
import com.msharialsayari.musrofaty.utils.SmsUtils
import com.msharialsayari.musrofaty.utils.enums.SmsType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SmsRepo @Inject constructor(
    private val dao: SmsDao,
    private val datasource: SmsDataSource,
    private val wordDetectorRepo: WordDetectorRepo,
    private val senderRepo: SenderRepo,
    @ApplicationContext val context: Context
) {

    suspend fun getAllSms(): List<SmsModel> {
        val senders = senderRepo.getAllActive().map { it.senderName }.toList()
        val list = mutableListOf<SmsModel>()
        senders.map {
            list.addAll(getSmsBySenderName(it))
        }
        return list
    }


    suspend fun getSmsBySenderName(bankName: String): List<SmsModel> {
        val list = mutableListOf<SmsModel>()
        dao.getSmsBySenderName(bankName).map {
            list.add(fillSmsModel(it.toSmsModel()))
        }
        return list
    }

    private suspend fun fillSmsModel(smsModel: SmsModel):SmsModel{
        smsModel.smsType = getSmsType(smsModel.body)
        smsModel.currency = getSmsCurrency(smsModel.body)
        return smsModel
    }


    private suspend fun getSmsType(body:String):SmsType{
        val expensesWord = wordDetectorRepo.getAllActive(WordDetectorType.EXPENSES_WORDS).map { it.word }
        val incomesWord = wordDetectorRepo.getAllActive(WordDetectorType.INCOME_WORDS).map { it.word }
        return SmsUtils.getSmsType(body, expensesList = expensesWord, incomesList = incomesWord )
    }

    private suspend fun getSmsCurrency(body:String):String{
        val currencyWord = wordDetectorRepo.getAllActive(WordDetectorType.CURRENCY_WORDS).map { it.word }
        return SmsUtils.getCurrency(body, currency = currencyWord )
    }


    suspend fun insert() {
        val smsList = datasource.loadBanksSms(context)
        val smsEntityList = mutableListOf<SmsEntity>()
        smsList.map { smsEntityList.add(it.toSmsEntity()) }
        dao.insertAll(*smsEntityList.toTypedArray())
    }

}