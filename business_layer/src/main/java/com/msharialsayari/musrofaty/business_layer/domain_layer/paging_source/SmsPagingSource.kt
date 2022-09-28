package com.msharialsayari.musrofaty.business_layer.domain_layer.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.toSmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.WordDetectorType
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SenderRepo
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.WordDetectorRepo
import com.msharialsayari.musrofaty.utils.SmsUtils
import com.msharialsayari.musrofaty.utils.enums.SmsType


private const val STARTING_KEY = 1

class SmsPagingSource(
    private val smsDao: SmsDao,
    private val wordDetectorRepo: WordDetectorRepo,
    private val senderRepo: SenderRepo,
    private val query: Int // senderId
) : PagingSource<Int, SmsModel>() {


    private fun ensureValidKey(key: Int) = STARTING_KEY.coerceAtLeast(key)



    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SmsModel> {
        // If params.key is null, it is the first load, so we start loading with STARTING_KEY
        var startKey = params.key ?: STARTING_KEY
        return try {
            val response = smsDao.getSmsPagedList(query).map { fillSmsModel(it.toSmsModel()) }.toList()
            LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = ++startKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

   override fun getRefreshKey(state: PagingState<Int, SmsModel>): Int? {
       val anchorPosition = state.anchorPosition ?: return null
       val sms = state.closestItemToPosition(anchorPosition) ?: return null
       return ensureValidKey(key = sms.id.toInt() - (state.config.pageSize / 2))
    }

    private suspend fun fillSmsModel(smsModel: SmsModel): SmsModel {
        smsModel.smsType = getSmsType(smsModel.body)
        smsModel.currency = getSmsCurrency(smsModel.body)
        smsModel.senderModel = getSender(smsModel.senderId)
        return smsModel
    }


    private suspend fun getSmsType(body: String): SmsType {
        val expensesWord = wordDetectorRepo.getAllActive(WordDetectorType.EXPENSES_WORDS).map { it.word }
        val incomesWord = wordDetectorRepo.getAllActive(WordDetectorType.INCOME_WORDS).map { it.word }
        return SmsUtils.getSmsType(body, expensesList = expensesWord, incomesList = incomesWord)
    }

    private suspend fun getSmsCurrency(body: String): String {
        val currencyWord = wordDetectorRepo.getAllActive(WordDetectorType.CURRENCY_WORDS).map { it.word }
        return SmsUtils.getCurrency(body, currency = currencyWord)
    }

    private suspend fun getSender(senderId: Int): SenderModel {
        return senderRepo.getSenderById(senderId)
    }


}