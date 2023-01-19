package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.toSmsModel
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreAndCategoryModel
import com.msharialsayari.musrofaty.business_layer.data_layer.sms.SmsDataSource
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.StoreModel
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
        val senders = senderRepo.getAllSenders().filter { it.isActive }
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
        val pagingSourceFactory = {
            when (filterOption) {
                DateUtils.FilterOption.ALL -> dao.getAllSms(senderId,query)
                DateUtils.FilterOption.TODAY -> dao.getTodaySms(senderId,query)
                DateUtils.FilterOption.WEEK -> dao.getCurrentWeekSms(senderId,query)
                DateUtils.FilterOption.MONTH -> dao.getCurrentMonthSms(senderId,query)
                DateUtils.FilterOption.YEAR -> dao.getCurrentYearSms(senderId,query)
                DateUtils.FilterOption.RANGE -> dao.getRangeDateSms(senderId,query,false,startDate,endDate)
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

    fun getSmsBySenderId(senderId: Int, filterOption: DateUtils.FilterOption = DateUtils.FilterOption.ALL, query:String="", startDate:Long = 0, endDate:Long= 0):  Flow<List<SmsEntity>> {
        return when (filterOption) {
            DateUtils.FilterOption.ALL -> dao.getSmsBySenderId(senderId,query)
            DateUtils.FilterOption.TODAY -> dao.getTodaySmsBySenderId(senderId,query)
            DateUtils.FilterOption.WEEK -> dao.getCurrentWeekSmsBySenderId(senderId,query)
            DateUtils.FilterOption.MONTH -> dao.getCurrentMonthSmsBySenderId(senderId,query)
            DateUtils.FilterOption.YEAR -> dao.getCurrentYearSmsBySenderId(senderId,query)
            DateUtils.FilterOption.RANGE -> dao.getRangeDateSmsBySenderId(senderId,query,startDate,endDate)
        }
    }

    fun getSmsBySenderIdWithSoftDeletedCheck(senderId: Int, isDeleted: Boolean,filterOption: DateUtils.FilterOption = DateUtils.FilterOption.ALL, query:String="", startDate:Long = 0, endDate:Long= 0):  Flow<List<SmsEntity>> {
        return when (filterOption) {
            DateUtils.FilterOption.ALL -> dao.getSmsBySenderIdWithSoftDeleteCheck(senderId,query,isDeleted)
            DateUtils.FilterOption.TODAY -> dao.getTodaySmsBySenderIdWithSoftDeleteCheck(senderId,query,isDeleted)
            DateUtils.FilterOption.WEEK -> dao.getCurrentWeekSmsBySenderIdWithSoftDeleteCheck(senderId,query,isDeleted)
            DateUtils.FilterOption.MONTH -> dao.getCurrentMonthSmsBySenderIdWithSoftDeleteCheck(senderId,query,isDeleted)
            DateUtils.FilterOption.YEAR -> dao.getCurrentYearSmsBySenderIdWithSoftDeleteCheck(senderId,query,isDeleted)
            DateUtils.FilterOption.RANGE -> dao.getRangeDateSmsBySenderIdWithSoftDeleteCheck(senderId,query,isDeleted,startDate,endDate)
        }
    }


    fun getAllFavoriteSms(senderId: Int, isFavorite:Boolean=true, filterOption: DateUtils.FilterOption = DateUtils.FilterOption.ALL, query:String="", startDate:Long = 0, endDate:Long= 0): Flow<PagingData<SmsEntity>> {

        val pagingSourceFactory = {
            when (filterOption) {
                DateUtils.FilterOption.ALL -> dao.getAllFavoriteSms(senderId,isFavorite,query )
                DateUtils.FilterOption.TODAY -> dao.getTodayFavoriteSms(senderId,isFavorite,query)
                DateUtils.FilterOption.WEEK -> dao.getCurrentWeekFavoriteSms(senderId,isFavorite,query)
                DateUtils.FilterOption.MONTH -> dao.getCurrentMonthFavoriteSms(senderId,isFavorite,query)
                DateUtils.FilterOption.YEAR -> dao.getCurrentYearFavoriteSms(senderId,isFavorite,query)
                DateUtils.FilterOption.RANGE -> dao.getRangeDateFavoriteSms(senderId,isFavorite,query, startDate, endDate )
            }
        }
        return Pager(
            config = PagingConfig(pageSize = ITEM_SIZE),
            pagingSourceFactory = pagingSourceFactory,
        ).flow

    }


    fun getAllSoftDeletedSms(senderId: Int, isDeleted:Boolean=true, filterOption: DateUtils.FilterOption = DateUtils.FilterOption.ALL, query:String="", startDate:Long = 0, endDate:Long= 0): Flow<PagingData<SmsEntity>> {

        val pagingSourceFactory = {
            when (filterOption) {
                DateUtils.FilterOption.ALL   -> dao.getAllSoftDeletedSms(senderId,isDeleted,query )
                DateUtils.FilterOption.TODAY -> dao.getTodaySoftDeletedSms(senderId,isDeleted,query)
                DateUtils.FilterOption.WEEK  -> dao.getCurrentWeekSoftDeletedSms(senderId,isDeleted,query)
                DateUtils.FilterOption.MONTH -> dao.getCurrentMonthSoftDeletedSms(senderId,isDeleted,query)
                DateUtils.FilterOption.YEAR  -> dao.getCurrentYearSoftDeletedSms(senderId,isDeleted,query)
                DateUtils.FilterOption.RANGE -> dao.getRangeDateSoftDeletedSms(senderId,isDeleted,query, startDate, endDate )
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