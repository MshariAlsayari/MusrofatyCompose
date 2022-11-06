package com.msharialsayari.musrofaty.jobs

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay

@HiltWorker
class InsertSmsJob  @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val smsRepo: SmsRepo,
): CoroutineWorker(appContext, workerParams){
    override suspend fun doWork(): Result {
        smsRepo.insert()
        Log.i("InsertSmsJob", "inserting..")
        delay(5000L)
        return Result.success()
    }
}