package com.msharialsayari.musrofaty.jobs

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import com.msharialsayari.musrofaty.utils.Constants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay

@HiltWorker
class InsertSmsJob  @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val smsRepo: SmsRepo,
): CoroutineWorker(appContext, workerParams){

    companion object {
        private val TAG = InsertSmsJob::class.java.simpleName
    }
    override suspend fun doWork(): Result {
        Log.d(TAG, "doWork() running...")

        if (runAttemptCount > Constants.ATTEMPTS_COUNT) {
            Log.d(TAG, "doWork() Result.failure")
            return Result.failure()
        }

        try {
            smsRepo.insert()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "doWork() Result.retry")
            return Result.retry()
        }

        Log.d(TAG, "doWork() Result.success")
        return Result.success()
    }
}