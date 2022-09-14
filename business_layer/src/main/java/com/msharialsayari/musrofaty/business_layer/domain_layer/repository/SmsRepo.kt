package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import android.content.Context
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.toSmsModel
import com.msharialsayari.musrofaty.business_layer.data_layer.sms.SmsDataSource
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toSmsEntity
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import com.msharialsayari.musrofaty.utils.WordsType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SmsRepo @Inject constructor(
    private val dao: SmsDao,
    private val datasource: SmsDataSource,
    @ApplicationContext val context: Context
) {

    suspend fun getAllSms(isDeleted: Boolean = false): List<SmsModel> {
        val banks = SharedPreferenceManager.getWordsList(context = context, WordsType.BANKS_WORDS)
        val list = mutableListOf<SmsModel>()
        dao.getAll(banks, isDeleted).forEach {
            list.add(it.toSmsModel())
        }
        return list
    }

//    suspend fun getAllSmsWithFilterOption(option:Int= R.id.all, isDeleted: Boolean = false):List<SmsModel>{
//        val allSms = getAllSms(isDeleted)
//        val finalList = when (option) {
//            R.id.all -> allSms
//            R.id.today -> SmsUtils.getTodaySms(allSms)
//            R.id.week -> SmsUtils.getCurrentWeek(allSms)
//            R.id.month -> SmsUtils.getCurrentMonth(allSms)
//            R.id.year -> SmsUtils.getCurrentYear(allSms)
//            else -> allSms
//        }
//
//        return finalList
//    }





    suspend fun getAllNoCheckIsDeleted(): List<SmsModel> {
        val banks = SharedPreferenceManager.getWordsList(context = context, WordsType.BANKS_WORDS)
        val list = mutableListOf<SmsModel>()
        dao.getAllNoCheckIsDeleted(banks).forEach {
            list.add(it.toSmsModel())
        }
        return list
    }


    suspend fun getSmsBySenderName(bankName: String, isDeleted: Boolean = false): List<SmsModel> {
        val list = mutableListOf<SmsModel>()
        dao.getSmsBySenderName(bankName, isDeleted).forEach {
            list.add(it.toSmsModel())
        }
        return list
    }

    suspend fun getSmsByBankNameNoCheck(bankName: String): List<SmsModel> {
        val list = mutableListOf<SmsModel>()
        dao.getSmsBySenderNameNoCheckIsDeleted(bankName).forEach {
            list.add(it.toSmsModel())
        }
        return list
    }

    suspend fun setDeletedSms(list: List<SmsModel>, isDeleted: Boolean): List<SmsModel> {
        val smsModel = mutableListOf<SmsModel>()
        val ids = mutableListOf<String>()
        list.forEach { sms ->
            ids.add(sms.id)
        }
        dao.setDeletedSms(ids, isDeleted)
        return smsModel
    }

    suspend fun insert() {
        val smsList = datasource.loadBanksSms(context)
        val smsEntityList = mutableListOf<SmsEntity>()
        smsList.forEach { sms ->
            val checkedSms = dao.getSms(sms.id)
            if (checkedSms == null || checkedSms.isDeleted == false ) {
                smsEntityList.add(sms.toSmsEntity())
            }
        }

        dao.insertAll(*smsEntityList.toTypedArray())
    }

}