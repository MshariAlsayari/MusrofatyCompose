package com.msharialsayari.musrofaty.jobs

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.WordDetectorModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.WordDetectorType
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.ContentRepo
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.FilterRepo
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SenderRepo
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.WordDetectorRepo
import com.msharialsayari.musrofaty.utils.Constants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class InitTransferWordsJob @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val wordDetectorRepo: WordDetectorRepo,
    private val contentRepo: ContentRepo,
    private val senderRepo: SenderRepo,
    private val filtersRepo: FilterRepo,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        initIncomesWords()
        initExpensesWords()
        return Result.success()
    }

    private suspend fun initIncomesWords() {
        wordDetectorRepo.delete(WordDetectorType.INCOME_WORDS.id)

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
        wordDetectorRepo.delete(WordDetectorType.EXPENSES_PURCHASES_WORDS.id)
        val expenses: List<WordDetectorModel> =
            Constants.listExpenseWords.map {
                WordDetectorModel(
                    word = it,
                    type = WordDetectorType.EXPENSES_PURCHASES_WORDS.name
                )
            }.toList()
        wordDetectorRepo.insert(expenses)

    }

}