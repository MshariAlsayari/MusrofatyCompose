package com.msharialsayari.musrofaty.jobs

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.InsertLatestSmsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class InsertLatestSmsJob@AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val insertLatestSmsUseCase: InsertLatestSmsUseCase,
): CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        insertLatestSmsUseCase.invoke()
        return Result.success()
    }
}