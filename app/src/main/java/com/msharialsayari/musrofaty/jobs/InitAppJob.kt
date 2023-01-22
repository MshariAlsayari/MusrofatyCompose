package com.msharialsayari.musrofaty.jobs

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.WordDetectorModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.ContentKey
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.SendersKey
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.WordDetectorType
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.*
import com.msharialsayari.musrofaty.utils.Constants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class InitAppJob @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val wordDetectorRepo: WordDetectorRepo,
    private val contentRepo: ContentRepo,
    private val senderRepo: SenderRepo,
    private val filtersRepo: FilterRepo,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        initContent()
        initSenders()
        initIncomesWords()
        initExpensesWords()
        initCurrencyWords()
        initFilters()
        return Result.success()
    }


    private suspend fun initContent() {
        val defaultContent = ContentModel.getDefaultContent()
        contentRepo.insert(*defaultContent.toTypedArray())

    }


    private suspend fun initIncomesWords() {
        val incomes: List<WordDetectorModel> =
            Constants.listIncomeWords.map {
                WordDetectorModel(
                    word = it,
                    type = WordDetectorType.INCOME_WORDS.name
                )
            }.toList()

        wordDetectorRepo.insert(incomes)
    }

    private suspend fun initExpensesWords() {
        val expenses: List<WordDetectorModel> =
            Constants.listExpenseWords.map {
                WordDetectorModel(
                    word = it,
                    type = WordDetectorType.EXPENSES_WORDS.name
                )
            }.toList()
        wordDetectorRepo.insert(expenses)

    }

    private suspend fun initCurrencyWords() {
        val currency: List<WordDetectorModel> =
            Constants.listCurrencyWords.map {
                WordDetectorModel(
                    word = it,
                    type = WordDetectorType.CURRENCY_WORDS.name
                )
            }.toList()

        wordDetectorRepo.insert(currency)


    }

    private suspend fun initSenders() {
        val sendersContent = contentRepo.getContentByKey(ContentKey.SENDERS.name)
        val banksSender = sendersContent.find { it.valueAr == SendersKey.BANKS.valueAr }
        val servicesSender = sendersContent.find { it.valueAr == SendersKey.SERVICES.valueAr }
        val digitalWalletSender = sendersContent.find { it.valueAr == SendersKey.DIGITALWALLET.valueAr }
        val tdawelSender = sendersContent.find { it.valueAr == SendersKey.TDAWEL.valueAr }
        val senders = SenderModel.getDefaultSendersModel(
            bankContentId = banksSender?.id ?: 0,
            servicesContentId = servicesSender?.id ?: 0,
            digitalWalletContentId = digitalWalletSender?.id ?: 0,
            tdawelSenderId = tdawelSender?.id ?: 0
        )
        senderRepo.insert(*senders.toTypedArray())

    }

    private suspend fun initFilters() {
        filtersRepo.migrateForFilters()
    }


}


