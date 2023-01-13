package com.msharialsayari.musrofaty.jobs

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.lifecycle.Observer
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.msharialsayari.musrofaty.base.Response
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.toCategoryModel
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.toStoreModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.CategoryRepo

import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.StoreRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber


@HiltWorker
class InitStoresJob @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val storeRepo: StoreRepo,

    ) : CoroutineWorker(appContext, workerParams) {

    private val scope = CoroutineScope(coroutineContext)
    companion object{
        private const val TAG = "InitStoresJob"
    }
    override suspend fun doWork(): Result {

        storeRepo.getStoresFromFirestore().collect{
            when (it) {
                is Response.Failure -> Timber.tag(TAG).d("Failure...")
                is Response.Loading -> Timber.tag(TAG).d("Loading...")
                is Response.Success -> insertList(it.data)
            }
        }
        return Result.success()
    }

    private fun insertList(stores: List<StoreEntity>) {
        val list = stores.map { it.toStoreModel() }.toList()
        scope.launch {
            storeRepo.insert(list)
        }

    }

}