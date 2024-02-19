package com.msharialsayari.musrofaty.jobs

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.*
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.InsertLatestSmsUseCase
import com.msharialsayari.musrofaty.utils.Constants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class InsertLatestSmsJob@AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val insertLatestSmsUseCase: InsertLatestSmsUseCase,
): CoroutineWorker(appContext, workerParams) {

    companion object {
        private val TAG = InsertLatestSmsJob::class.java.simpleName
    }

    override suspend fun doWork(): Result {

        Log.d(TAG, "doWork() running...")

        if (runAttemptCount > Constants.ATTEMPTS_COUNT) {
            Log.d(TAG, "doWork() Result.failure")
            return Result.failure()
        }

        try {
           val sms =  insertLatestSmsUseCase.invoke()
            sms?.let {
                val inputData = Data.Builder()
                    .putString(AutoCategoriesStoresJob.STORE_NAME_KEY, it.storeName)
                    .build()

                val worker = OneTimeWorkRequestBuilder<AutoCategoriesStoresJob>()
                        .setInputData(inputData)
                        .build()

                WorkManager.getInstance(appContext).enqueue(worker)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "doWork() Result.retry")
            return Result.retry()
        }

        Log.d(TAG, "doWork() Result.success")
        return Result.success()
    }
}