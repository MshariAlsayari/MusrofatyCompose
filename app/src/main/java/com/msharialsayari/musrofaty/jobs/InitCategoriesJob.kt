package com.msharialsayari.musrofaty.jobs

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.CategoryRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class InitCategoriesJob @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val categoryRepo: CategoryRepo,

) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        categoryRepo.insertDefaultCategoryList()
        return Result.success()
    }

}