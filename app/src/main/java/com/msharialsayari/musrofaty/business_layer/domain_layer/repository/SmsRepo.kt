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
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.*
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
    private val storeFirebaseRepo:StoreFirebaseRepo,
    @ApplicationContext val context: Context
) {


    suspend fun fillSmsModel(smsModel: SmsModel): SmsModel {
        smsModel.smsType               = getSmsType(smsModel.body)
        smsModel.currency              = getSmsCurrency(smsModel.body)
        smsModel.amount                = getAmount(smsModel.body)
        smsModel.senderModel           = getSender(smsModel.senderId)
        smsModel.storeAndCategoryModel = getStoreAndCategory(smsModel.storeName)
        return smsModel
    }

    suspend fun favoriteSms(smsId: String, favorite: Boolean) {
        return dao.favoriteSms(smsId, favorite)
    }

    suspend fun softDeleteSms(smsId: String, delete: Boolean){
        return dao.softDelete(smsId, delete)
    }


    suspend fun getSms(smsId: String) :SmsModel?{
        val sms = dao.getSms(smsId)
        sms?.let {
            return fillSmsModel(it.toSmsModel())
        }
        return null

    }

    suspend fun getAllSmsForAllSenders(isDeleted:Boolean=false,filterOption: DateUtils.FilterOption = DateUtils.FilterOption.ALL, query:String="", startDate:Long = 0, endDate:Long= 0 ): List<SmsEntity> {
        val senders = senderRepo.getAllSenders()
        val smsList  = mutableListOf<SmsEntity>()
        senders.forEach {

            val list = when (filterOption) {
                DateUtils.FilterOption.ALL -> dao.getAll(it.id, query,isDeleted)
                DateUtils.FilterOption.TODAY -> dao.getToday(it.id, query,isDeleted)
                DateUtils.FilterOption.WEEK -> dao.getCurrentWeek(it.id, query,isDeleted)
                DateUtils.FilterOption.MONTH -> dao.getCurrentMonth(it.id, query,isDeleted)
                DateUtils.FilterOption.YEAR -> dao.getCurrentYear(it.id, query,isDeleted)
                DateUtils.FilterOption.RANGE -> dao.getRangeDate(
                    it.id,
                    query,
                    isDeleted,
                    startDate,
                    endDate
                )
            }


            smsList.addAll(list)

        }

        return smsList

    }

     fun getAllSmsForDashboard(filterOption: DateUtils.FilterOption = DateUtils.FilterOption.ALL, query:String="", startDate:Long = 0, endDate:Long= 0 ):  Flow<PagingData<SmsEntity>> {
         val pagingSourceFactory = {
             when (filterOption) {
                 DateUtils.FilterOption.ALL -> dao.getAllDashboard(query)
                 DateUtils.FilterOption.TODAY -> dao.getTodayDashboard(query)
                 DateUtils.FilterOption.WEEK -> dao.getCurrentWeekDashboard(query)
                 DateUtils.FilterOption.MONTH -> dao.getCurrentMonthDashboard(query)
                 DateUtils.FilterOption.YEAR -> dao.getCurrentYearDashboard(query)
                 DateUtils.FilterOption.RANGE -> dao.getRangeDateDashboard(
                     query,
                     startDate,
                     endDate
                 )
             }
         }

         return Pager(
             config = PagingConfig(pageSize = ITEM_SIZE),
             pagingSourceFactory = pagingSourceFactory,
         ).flow

    }

    suspend fun getAll(senderId: Int, filterOption: DateUtils.FilterOption = DateUtils.FilterOption.ALL, query:String="", startDate:Long = 0, endDate:Long= 0 ): List<SmsModel> {
        val returnedList = mutableListOf<SmsModel>()
        val smsListEntity =
            when (filterOption) {
                DateUtils.FilterOption.ALL -> dao.getAll(senderId,query)
                DateUtils.FilterOption.TODAY -> dao.getToday(senderId,query)
                DateUtils.FilterOption.WEEK -> dao.getCurrentWeek(senderId,query)
                DateUtils.FilterOption.MONTH -> dao.getCurrentMonth(senderId,query)
                DateUtils.FilterOption.YEAR -> dao.getCurrentYear(senderId,query)
                DateUtils.FilterOption.RANGE -> dao.getRangeDate(senderId,query,false,startDate,endDate)
            }

        smsListEntity.map {
            returnedList.add(fillSmsModel( it.toSmsModel()))
        }

        return returnedList

    }


     fun getAllSms(senderId: Int, filterOption: DateUtils.FilterOption = DateUtils.FilterOption.ALL, query:String="", startDate:Long = 0, endDate:Long= 0 ): Flow<PagingData<SmsEntity>> {

         // List of bind parameters
         val args: MutableList<Any> = mutableListOf();

         // Beginning of query
         var queryString = "SELECT * FROM SmsEntity WHERE senderId =? AND isDeleted=false AND"
         args.add(senderId)






         // Build filter body query
         var filterBodyQuery = ""
         val filtersList = FilterAdvancedModel.getFilterWordsAsList(query)
         if (filtersList.isEmpty()){
             filterBodyQuery = " body LIKE '%' || ? || '%'"
             args.add(query)
         }else{
             filtersList.forEachIndexed { index, filter ->
                 if (index == 0){
                     filterBodyQuery  = " body LIKE '%' || ? || '%'"
                 }else{
                     filterBodyQuery  += " AND body LIKE '%' || ? || '%'"
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


         val finalQuery = SimpleSQLiteQuery(queryString, args.toList().toTypedArray())

         val pagingSourceFactory = {
            when (filterOption) {
                DateUtils.FilterOption.ALL -> dao.getAllSms(finalQuery)
                DateUtils.FilterOption.TODAY -> dao.getTodaySms(finalQuery)
                DateUtils.FilterOption.WEEK -> dao.getCurrentWeekSms(finalQuery)
                DateUtils.FilterOption.MONTH -> dao.getCurrentMonthSms(finalQuery)
                DateUtils.FilterOption.YEAR -> dao.getCurrentYearSms(finalQuery)
                DateUtils.FilterOption.RANGE -> dao.getRangeDateSms(finalQuery)
            }
        }


        return Pager(
            config = PagingConfig(pageSize = ITEM_SIZE),
            pagingSourceFactory = pagingSourceFactory,
        ).flow

    }

    fun getAllSms(query:String=""): Flow<PagingData<SmsEntity>> {
        val pagingSourceFactory = { dao.getAllSms(query) }

        return Pager(
            config = PagingConfig(pageSize = ITEM_SIZE),
            pagingSourceFactory = pagingSourceFactory,
        ).flow

    }



    fun getSmsBySenderIdWithSoftDeletedCheck(senderId: Int, isDeleted: Boolean,filterOption: DateUtils.FilterOption = DateUtils.FilterOption.ALL, query:String="", startDate:Long = 0, endDate:Long= 0):  Flow<List<SmsEntity>> {

        // List of bind parameters
        val args: MutableList<Any> = mutableListOf();

        // Beginning of query
        var queryString = "SELECT * FROM SmsEntity WHERE senderId =? AND isDeleted=? AND"
        args.add(senderId)
        args.add(isDeleted)





        // Build filter body query
        var filterBodyQuery = ""
        val filtersList = FilterAdvancedModel.getFilterWordsAsList(query)
        if (filtersList.isEmpty()){
            filterBodyQuery = " body LIKE '%' || ? || '%'"
            args.add(query)
        }else{
            filtersList.forEachIndexed { index, filter ->
                if (index == 0){
                    filterBodyQuery  = " body LIKE '%' || ? || '%'"
                }else{
                    filterBodyQuery  += " AND body LIKE '%' || ? || '%'"
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


        val finalQuery = SimpleSQLiteQuery(queryString, args.toList().toTypedArray())


        return when (filterOption) {
            DateUtils.FilterOption.ALL -> dao.getSmsBySenderIdWithSoftDeleteCheck(finalQuery)
            DateUtils.FilterOption.TODAY -> dao.getTodaySmsBySenderIdWithSoftDeleteCheck(finalQuery)
            DateUtils.FilterOption.WEEK -> dao.getCurrentWeekSmsBySenderIdWithSoftDeleteCheck(finalQuery)
            DateUtils.FilterOption.MONTH -> dao.getCurrentMonthSmsBySenderIdWithSoftDeleteCheck(finalQuery)
            DateUtils.FilterOption.YEAR -> dao.getCurrentYearSmsBySenderIdWithSoftDeleteCheck(finalQuery)
            DateUtils.FilterOption.RANGE -> dao.getRangeDateSmsBySenderIdWithSoftDeleteCheck(finalQuery)
        }
    }


    fun getAllFavoriteSms(senderId: Int, filterOption: DateUtils.FilterOption = DateUtils.FilterOption.ALL, query:String="", startDate:Long = 0, endDate:Long= 0): Flow<PagingData<SmsEntity>> {

        // List of bind parameters
        val args: MutableList<Any> = mutableListOf();

        // Beginning of query
        var queryString = "SELECT * FROM SmsEntity WHERE senderId =? AND isFavorite=true AND"
        args.add(senderId)






        // Build filter body query
        var filterBodyQuery = ""
        val filtersList = FilterAdvancedModel.getFilterWordsAsList(query)
        if (filtersList.isEmpty()){
            filterBodyQuery = " body LIKE '%' || ? || '%'"
            args.add(query)
        }else{
            filtersList.forEachIndexed { index, filter ->
                if (index == 0){
                    filterBodyQuery  = " body LIKE '%' || ? || '%'"
                }else{
                    filterBodyQuery  += " AND body LIKE '%' || ? || '%'"
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


        val finalQuery = SimpleSQLiteQuery(queryString, args.toList().toTypedArray())

        val pagingSourceFactory = {
            when (filterOption) {
                DateUtils.FilterOption.ALL -> dao.getAllFavoriteSms(finalQuery)
                DateUtils.FilterOption.TODAY -> dao.getTodayFavoriteSms(finalQuery)
                DateUtils.FilterOption.WEEK -> dao.getCurrentWeekFavoriteSms(finalQuery)
                DateUtils.FilterOption.MONTH -> dao.getCurrentMonthFavoriteSms(finalQuery)
                DateUtils.FilterOption.YEAR -> dao.getCurrentYearFavoriteSms(finalQuery)
                DateUtils.FilterOption.RANGE -> dao.getRangeDateFavoriteSms(finalQuery)
            }
        }
        return Pager(
            config = PagingConfig(pageSize = ITEM_SIZE),
            pagingSourceFactory = pagingSourceFactory,
        ).flow

    }


    fun getAllSoftDeletedSms(senderId: Int, filterOption: DateUtils.FilterOption = DateUtils.FilterOption.ALL, query:String="", startDate:Long = 0, endDate:Long= 0): Flow<PagingData<SmsEntity>> {


        // List of bind parameters
        val args: MutableList<Any> = mutableListOf();

        // Beginning of query
        var queryString = "SELECT * FROM SmsEntity WHERE senderId =? AND isDeleted=true AND"
        args.add(senderId)






        // Build filter body query
        var filterBodyQuery = ""
        val filtersList = FilterAdvancedModel.getFilterWordsAsList(query)
        if (filtersList.isEmpty()){
            filterBodyQuery = " body LIKE '%' || ? || '%'"
            args.add(query)
        }else{
            filtersList.forEachIndexed { index, filter ->
                if (index == 0){
                    filterBodyQuery  = " body LIKE '%' || ? || '%'"
                }else{
                    filterBodyQuery  += " AND body LIKE '%' || ? || '%'"
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


        val finalQuery = SimpleSQLiteQuery(queryString, args.toList().toTypedArray())

        val pagingSourceFactory = {
            when (filterOption) {
                DateUtils.FilterOption.ALL   -> dao.getAllSoftDeletedSms(finalQuery)
                DateUtils.FilterOption.TODAY -> dao.getTodaySoftDeletedSms(finalQuery)
                DateUtils.FilterOption.WEEK  -> dao.getCurrentWeekSoftDeletedSms(finalQuery)
                DateUtils.FilterOption.MONTH -> dao.getCurrentMonthSoftDeletedSms(finalQuery)
                DateUtils.FilterOption.YEAR  -> dao.getCurrentYearSoftDeletedSms(finalQuery)
                DateUtils.FilterOption.RANGE -> dao.getRangeDateSoftDeletedSms(finalQuery)
            }
        }
        return Pager(
            config = PagingConfig(pageSize = ITEM_SIZE),
            pagingSourceFactory = pagingSourceFactory,
        ).flow

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
        val smsEntityList = mutableListOf<SmsEntity>()
        smsList.map {
            smsEntityList.add(it.toSmsEntity())
            insertStore(it)
        }
        dao.insertAll(*smsEntityList.toTypedArray())
    }

    suspend fun insert(senderName:String) {
        val smsList = datasource.loadBanksSms(context,senderName)
        val smsEntityList = mutableListOf<SmsEntity>()
        smsList.map {
            smsEntityList.add(it.toSmsEntity())
            insertStore(it)
        }
        dao.insertAll(*smsEntityList.toTypedArray())
    }

    private suspend fun insertStore(sms: SmsModel) {
        val store = storeRepo.getStoreByStoreName(sms.storeName)
        if (sms.storeName.isNotEmpty() && (store == null || store.categoryId == 0) ) {
            val storeFirebase = storeFirebaseRepo.getStoreByStoreName(sms.storeName)
            val model = if (storeFirebase != null) StoreModel(name = sms.storeName, categoryId = storeFirebase.category_id) else StoreModel(name = sms.storeName)
            storeRepo.insertStore(model)
        }
    }




    suspend fun deleteSenderSms(senderId:Int){
        dao.deleteSenderSms(senderId)
    }




}