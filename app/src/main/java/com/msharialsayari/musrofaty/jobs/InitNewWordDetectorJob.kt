package com.msharialsayari.musrofaty.jobs

import android.content.Context
import android.util.Log
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
class InitNewWordDetectorJob @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val wordDetectorRepo: WordDetectorRepo,
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        private val TAG = InitNewWordDetectorJob::class.java.simpleName
    }

    override suspend fun doWork(): Result {

        Log.d(TAG, "doWork() running...")

        if (runAttemptCount > Constants.ATTEMPTS_COUNT) {
            Log.d(TAG, "doWork() Result.failure")
            return Result.failure()
        }


        try {
            initAmountsWords()
            initStoresWords()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "doWork() Result.retry")
            return Result.retry()
        }


        Log.d(TAG, "doWork() Result.success")
        return Result.success()
    }

    private suspend fun initAmountsWords() {
        val incomes: List<WordDetectorModel> =
            Constants.listAmountWords.map {
                WordDetectorModel(
                    word = it,
                    type = WordDetectorType.AMOUNT_WORDS.name
                )
            }.toList()

        wordDetectorRepo.insert(incomes)
    }

    private suspend fun initStoresWords() {
        val incomes: List<WordDetectorModel> =
            Constants.listStoreWords.map {
                WordDetectorModel(
                    word = it,
                    type = WordDetectorType.STORE_WORDS.name
                )
            }.toList()

        wordDetectorRepo.insert(incomes)
    }
}