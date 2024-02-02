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
class InitWithdrawalWordsJob @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val wordDetectorRepo: WordDetectorRepo,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        initWithdrawalATMWords()
        return Result.success()
    }

    private suspend fun initWithdrawalATMWords() {
        val incomes: List<WordDetectorModel> =
            Constants.listWithdrawalATMWords.map {
                WordDetectorModel(
                    word = it,
                    type = WordDetectorType.WITHDRAWAL_ATM_WORDS.name
                )
            }.toList()

        wordDetectorRepo.insert(incomes)
    }
}