package com.msharialsayari.musrofaty.jobs

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.WordDetectorModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.WordDetectorType
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.WordDetectorRepo
import com.msharialsayari.musrofaty.utils.Constants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class InitTransferWordsJob @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val wordDetectorRepo: WordDetectorRepo,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        wordDetectorRepo.delete(WordDetectorType.INCOME_WORDS.id)
        wordDetectorRepo.delete(WordDetectorType.EXPENSES_PURCHASES_WORDS.id)
        initIncomesWords()
        initExpensesPurchasesWords()
        initExpensesOutGoingTransferWords()
        initExpensesPayBillsWords()
        return Result.success()
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

    private suspend fun initExpensesPurchasesWords() {
        val expenses: List<WordDetectorModel> =
            Constants.listExpenseWords.map {
                WordDetectorModel(
                    word = it,
                    type = WordDetectorType.EXPENSES_PURCHASES_WORDS.name
                )
            }.toList()
        wordDetectorRepo.insert(expenses)

    }

    private suspend fun initExpensesOutGoingTransferWords() {
        val expenses: List<WordDetectorModel> =
            Constants.listExpenseOutgoingTransferWords.map {
                WordDetectorModel(
                    word = it,
                    type = WordDetectorType.EXPENSES_OUTGOING_TRANSFER_WORDS.name
                )
            }.toList()
        wordDetectorRepo.insert(expenses)

    }

    private suspend fun initExpensesPayBillsWords() {
        val expenses: List<WordDetectorModel> =
            Constants.listExpensePayBillsWords.map {
                WordDetectorModel(
                    word = it,
                    type = WordDetectorType.EXPENSES_PAY_BILLS_WORDS.name
                )
            }.toList()
        wordDetectorRepo.insert(expenses)

    }

}