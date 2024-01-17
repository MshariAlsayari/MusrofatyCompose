package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.toSmsModel
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreAndCategoryModel
import com.msharialsayari.musrofaty.business_layer.data_layer.sms.SmsDataSource
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterAdvancedModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.WordDetectorType
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
    private val storeRepo: StoreRepo,
    @ApplicationContext val context: Context
) {


    suspend fun fillSmsModel(smsModel: SmsModel): SmsModel {
        smsModel.smsType = getSmsType(smsModel.body)
        smsModel.currency = getSmsCurrency(smsModel.body)
        smsModel.amount = getAmount(smsModel.body)
        smsModel.senderModel = getSender(smsModel.senderId)
        smsModel.storeAndCategoryModel = getStoreAndCategory(smsModel.storeName)
        return smsModel
    }

    suspend fun favoriteSms(smsId: String, favorite: Boolean) {
        return dao.favoriteSms(smsId, favorite)
    }

    suspend fun softDeleteSms(smsId: String, delete: Boolean){
        return dao.softDelete(smsId, delete)
    }

    suspend fun getSms(smsId: String): SmsModel? {
        val sms = dao.getSms(smsId)
        sms?.let {
            return fillSmsModel(it.toSmsModel())
        }
        return null

    }

    suspend fun getAllSmsForAllSenders(
        filterOption: DateUtils.FilterOption = DateUtils.FilterOption.ALL,
        isDeleted: Boolean?=null,
        isFavorite:Boolean?=null,
        query: String = "",
        startDate: Long = 0,
        endDate: Long = 0
    ): List<SmsEntity> {
        val senders = senderRepo.getAllSenders()
        val smsList = mutableListOf<SmsEntity>()
        senders.forEach {
            val list = getAllSms(
                senderId = it.id,
                filterOption = filterOption,
                isDeleted = isDeleted,
                isFavorite=isFavorite,
                query = query,
                startDate = startDate,
                endDate = endDate
            ).toList()
            smsList.addAll(list)
        }
        return smsList

    }

    suspend fun getAllSms(
        senderId: Int?= null,
        filterOption: DateUtils.FilterOption = DateUtils.FilterOption.ALL,
        isDeleted: Boolean?=null,
        isFavorite:Boolean?=null,
        query: String = "",
        startDate: Long = 0,
        endDate: Long = 0
    ): List<SmsEntity> {

        val finalQuery = getSmsQuery(
            senderId = senderId,
            filterOption = filterOption,
            isDeleted = isDeleted,
            isFavorite = isFavorite,
            query = query,
            startDate = startDate,
            endDate = endDate
        )
        return dao.getAllSms(finalQuery)
    }



    suspend fun getAllSmsModel(
        senderId: Int?= null,
        filterOption: DateUtils.FilterOption = DateUtils.FilterOption.ALL,
        isDeleted: Boolean?=null,
        isFavorite:Boolean?=null,
        query: String = "",
        startDate: Long = 0,
        endDate: Long = 0
    ): List<SmsModel> {

        val finalQuery = getSmsQuery(
            senderId = senderId,
            filterOption = filterOption,
            isDeleted = isDeleted,
            isFavorite = isFavorite,
            query = query,
            startDate = startDate,
            endDate = endDate
        )

        val returnedList = mutableListOf<SmsModel>()
        val smsListEntity = dao.getAllSms(finalQuery)

        smsListEntity.map {
            returnedList.add(fillSmsModel( it.toSmsModel()))
        }

        return returnedList
    }


    fun observingPaginationAllSms(
        senderId: Int?= null,
        filterOption: DateUtils.FilterOption = DateUtils.FilterOption.ALL,
        isDeleted: Boolean?=null,
        isFavorite:Boolean?=null,
        query: String = "",
        startDate: Long = 0,
        endDate: Long = 0
    ): Flow<PagingData<SmsEntity>> {

        val finalQuery = getSmsQuery(
            senderId = senderId,
            filterOption = filterOption,
            isDeleted = isDeleted,
            isFavorite = isFavorite,
            query = query,
            startDate = startDate,
            endDate = endDate
        )


        val pagingSourceFactory = { dao.getPaginationAllSms(finalQuery) }


        return Pager(
            config = PagingConfig(pageSize = ITEM_SIZE),
            pagingSourceFactory = pagingSourceFactory,
        ).flow

    }


    fun observingPaginationAllSms(query: String = ""): Flow<PagingData<SmsEntity>> {
        val pagingSourceFactory = { dao.getPaginationAllSms(query) }

        return Pager(
            config = PagingConfig(pageSize = ITEM_SIZE),
            pagingSourceFactory = pagingSourceFactory,
        ).flow

    }


    fun observingAllSms(
        senderId: Int?= null,
        filterOption: DateUtils.FilterOption = DateUtils.FilterOption.ALL,
        isDeleted: Boolean?=null,
        isFavorite:Boolean?=null,
        query: String = "",
        startDate: Long = 0,
        endDate: Long = 0
    ): Flow<List<SmsEntity>> {

        val finalQuery = getSmsQuery(
            senderId = senderId,
            filterOption = filterOption,
            isDeleted = isDeleted,
            isFavorite = isFavorite,
            query = query,
            startDate = startDate,
            endDate = endDate
        )

        return dao.observingSmsList(finalQuery)
    }



   private suspend fun getSmsType(body: String): SmsType {
        val expensesWord = wordDetectorRepo.getAll(WordDetectorType.EXPENSES_WORDS).map { it.word }
        val incomesWord = wordDetectorRepo.getAll(WordDetectorType.INCOME_WORDS).map { it.word }
        return SmsUtils.getSmsType(body, expensesList = expensesWord, incomesList = incomesWord)
    }

    private suspend fun getSmsCurrency(body: String): String {
        val currencyWord = wordDetectorRepo.getAll(WordDetectorType.CURRENCY_WORDS).map { it.word }
        return SmsUtils.getCurrency(body, currencyList = currencyWord)
    }

    private suspend fun getAmount(body: String): Double {
        val currencyWord = wordDetectorRepo.getAll(WordDetectorType.CURRENCY_WORDS).map { it.word }
        return SmsUtils.extractAmount(body, currencyList = currencyWord)
    }

    private suspend fun getSender(senderId: Int): SenderModel? {
        return senderRepo.getSenderById(senderId)
    }

    private suspend fun getStoreAndCategory(storeName: String): StoreAndCategoryModel {
        return storeRepo.getStoreAndCategory(storeName)
    }


    suspend fun insert() {
        val smsList = datasource.loadBanksSms(context)
        dao.insertAll(*smsList.toTypedArray())
    }

    suspend fun insertSenderSMSs(senderName: String) {
        val smsList = datasource.loadBanksSms(context, senderName)
        dao.insertAll(*smsList.toTypedArray())
    }


    suspend fun deleteSenderSms(senderId: Int) {
        dao.deleteSenderSms(senderId)
    }

    private fun getSmsQuery(
        senderId: Int? = null,
        filterOption: DateUtils.FilterOption = DateUtils.FilterOption.ALL,
        isDeleted: Boolean? = null,
        isFavorite: Boolean? = null,
        query: String = "",
        startDate: Long = 0,
        endDate: Long = 0
    ): SimpleSQLiteQuery {

        val args: MutableList<Any> = mutableListOf()
        var queryString = "SELECT * FROM SmsEntity "

        //adding where
        if(senderId != null || isDeleted != null || isFavorite != null){
            queryString+=" WHERE "
        }

        if(senderId!= null){
            queryString+= " senderId=? "
            args.add(senderId)
        }

        if(isDeleted!= null){
            queryString += if(senderId != null){ " AND isDeleted=$isDeleted  "
            }else{
                " isDeleted=$isDeleted "
            }
        }

        if(isFavorite!= null){
            queryString += if(senderId != null || isDeleted != null){
                " AND isFavorite=$isFavorite "
            }else{
                " isFavorite=$isFavorite "
            }
        }

        queryString += if(senderId != null || isDeleted != null || isFavorite != null){
            " AND "
        }else{
            " WHERE "
        }

        // Build filter body query
        var filterBodyQuery = ""
        val filtersList = FilterAdvancedModel.getFilterWordsAsList(query)
        if (filtersList.isEmpty()) {
            filterBodyQuery = " body LIKE '%' || ? || '%'"
            args.add(query)
        } else {
            filtersList.forEachIndexed { index, filter ->
                if (index == 0) {
                    filterBodyQuery = " body LIKE '%' || ? || '%'"
                } else {
                    filterBodyQuery += " AND body LIKE '%' || ? || '%'"
                }
                args.add(filter)

            }


        }
        queryString += filterBodyQuery

        // Build filter date query
        when (filterOption) {
            DateUtils.FilterOption.ALL -> queryString += ""
            DateUtils.FilterOption.TODAY -> queryString += " AND  strftime('%d-%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%d-%m-%Y', date('now','localtime'))"
            DateUtils.FilterOption.WEEK -> queryString += " AND  strftime('%W-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%W-%Y', date('now','localtime'))"
            DateUtils.FilterOption.MONTH -> queryString += " AND  strftime('%m-%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%m-%Y', date('now'))"
            DateUtils.FilterOption.YEAR -> queryString += " AND  strftime('%Y', date(timestamp/1000,'unixepoch', 'localtime')) =  strftime('%Y', date('now'))"
            DateUtils.FilterOption.RANGE -> {
                queryString += " AND  strftime('%Y-%m-%d', date(timestamp/1000,'unixepoch', 'localtime')) BETWEEN   strftime('%Y-%m-%d', date(?/1000,'unixepoch', 'localtime')) AND strftime('%Y-%m-%d', date(?/1000,'unixepoch', 'localtime'))"
                args.add(startDate)
                args.add(endDate)


            }
        }

        // End of query string
        queryString += " order by timestamp DESC"
        return SimpleSQLiteQuery(queryString, args.toList().toTypedArray())
    }

    suspend fun getAllSmsContainsStore(storeName: String = ""): List<SmsModel> {
         return dao.getAllSmsContainsStore(storeName).map {
             fillSmsModel( it.toSmsModel())
         }.toList()
    }




}