package com.msharialsayari.musrofaty.jobs

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.lifecycle.Observer
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.msharialsayari.musrofaty.base.Response
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.toCategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.CategoryRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber


@HiltWorker
class InitCategoriesJob @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val categoryRepo: CategoryRepo,

) : CoroutineWorker(appContext, workerParams) {


    private val scope = CoroutineScope(coroutineContext)

    companion object{
        private const val TAG = "InitCategoriesJob"
    }
    override suspend fun doWork(): Result {
        categoryRepo.getCategoriesFromFirestore().collect{
            when (it) {
                is Response.Failure -> Timber.tag(TAG).d("Failure...")
                is Response.Loading -> Timber.tag(TAG).d("Loading...")
                is Response.Success -> insertList(it.data)
            }
        }

        delay(5000)
        return Result.success()
    }

    private fun insertList(categories: List<CategoryEntity>) {
        Timber.tag(TAG).d( "insertList()..." + "categories:" + categories.size)

        val list = categories.map {
            Log.d(TAG, it.id.toString())
            it.toCategoryModel()
        }.toList()
        scope.launch {
            categoryRepo.insert(list)
        }

    }

}