package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.sms.SmsDataSource
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.WordDetectorType
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toSmsEntity
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.SmsUtils
import com.msharialsayari.musrofaty.utils.enums.SmsType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

const val ITEM_SIZE = 10

@Singleton
class SmsRepo @Inject constructor(
    private val dao: SmsDao,
    private val datasource: SmsDataSource,
    private val wordDetectorRepo: WordDetectorRepo,
    private val senderRepo: SenderRepo,
    @ApplicationContext val context: Context
) {


    private suspend fun fillSmsModel(smsModel: SmsModel): SmsModel {
        smsModel.smsType = getSmsType(smsModel.body)
        smsModel.currency = getSmsCurrency(smsModel.body)
        smsModel.senderModel = getSender(smsModel.senderId)
        return smsModel
    }

    suspend fun favoriteSms(smsId: String, favorite: Boolean) {
        return dao.favoriteSms(smsId, favorite)
    }


     fun getAllSms(senderId: Int, filterOption: DateUtils.FilterOption = DateUtils.FilterOption.ALL): Flow<PagingData<SmsEntity>> {
        val pagingSourceFactory = {
            when (filterOption) {
                DateUtils.FilterOption.ALL -> dao.getAllSms(senderId)
                DateUtils.FilterOption.TODAY -> dao.getAllSms(
                    senderId,
                    DateUtils.getYesterday(),
                    DateUtils.getTomorrow()
                )
                DateUtils.FilterOption.WEEK -> dao.getAllSms(
                    senderId,
                    DateUtils.getYesterday(),
                    DateUtils.getTomorrow()
                )
                DateUtils.FilterOption.MONTH -> dao.getAllSms(
                    senderId,
                    DateUtils.getYesterday(),
                    DateUtils.getTomorrow()
                )
                DateUtils.FilterOption.YEAR -> dao.getAllSms(
                    senderId,
                    DateUtils.getYesterday(),
                    DateUtils.getTomorrow()
                )
                DateUtils.FilterOption.RANGE -> dao.getAllSms(
                    senderId,
                    DateUtils.getYesterday(),
                    DateUtils.getTomorrow()
                )
            }
        }


        return Pager(
            config = PagingConfig(pageSize = ITEM_SIZE),
            pagingSourceFactory = pagingSourceFactory,
        ).flow

    }

    fun getSmsBySenderId(senderId: Int):  Flow<List<SmsEntity>> {
        return dao.getSmsBySenderId(senderId)
    }


    fun getAllFavoriteSms(senderId: Int, isFavorite:Boolean=true): Flow<PagingData<SmsEntity>> {
        val pagingSourceFactory = {dao.getAllFavoriteSms(senderId,isFavorite)}
        return Pager(
            config = PagingConfig(pageSize = ITEM_SIZE),
            pagingSourceFactory = pagingSourceFactory,
        ).flow

    }





    private suspend fun getSmsType(body: String): SmsType {
        val expensesWord = wordDetectorRepo.getAllActive(WordDetectorType.EXPENSES_WORDS).map { it.word }
        val incomesWord = wordDetectorRepo.getAllActive(WordDetectorType.INCOME_WORDS).map { it.word }
        return SmsUtils.getSmsType(body, expensesList = expensesWord, incomesList = incomesWord)
    }

    private suspend fun getSmsCurrency(body: String): String {
        val currencyWord =
            wordDetectorRepo.getAllActive(WordDetectorType.CURRENCY_WORDS).map { it.word }
        return SmsUtils.getCurrency(body, currency = currencyWord)
    }

    private suspend fun getSender(senderId: Int): SenderModel {
        return senderRepo.getSenderById(senderId)
    }


    suspend fun insert() {
        val smsList = datasource.loadBanksSms(context)
        val smsEntityList = mutableListOf<SmsEntity>()
        smsList.map { smsEntityList.add(it.toSmsEntity()) }
        dao.insertAll(*smsEntityList.toTypedArray())
    }


}