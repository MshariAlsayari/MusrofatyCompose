package com.msharialsayari.musrofaty.jobs

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.CategoryRepo
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class InsertCategoryJob @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val category: CategoryRepo
):
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        category.insertDefaultCategoryList()
        SharedPreferenceManager.setFirstLunchCategory(appContext, true)
        return Result.success()
    }


}