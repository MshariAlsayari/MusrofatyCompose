package com.msharialsayari.musrofaty.jobs

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.SmsRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class InsertSmsJob  @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val smsRepo: SmsRepo,
): CoroutineWorker(appContext, workerParams){
    override suspend fun doWork(): Result {
        smsRepo.insert()
        return Result.success()
    }
}