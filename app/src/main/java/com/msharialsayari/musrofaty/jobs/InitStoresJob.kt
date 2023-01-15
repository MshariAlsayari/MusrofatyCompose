package com.msharialsayari.musrofaty.jobs

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.msharialsayari.musrofaty.base.Response
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_firebase_database.StoreFirebaseEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.StoreFirebaseRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@HiltWorker
class InitStoresJob @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val storeRepo: StoreFirebaseRepo,

    ) : CoroutineWorker(appContext, workerParams) {

    private val scope = CoroutineScope(coroutineContext)
    companion object{
        private const val TAG = "InitStoresJob"
    }
    override suspend fun doWork(): Result {

        storeRepo.getStoresFromFirestore().collect{
            when (it) {
                is Response.Failure -> Log.d(TAG, "Failure... " + it.errorMessage)
                is Response.Loading -> Log.d(TAG, "Loading...")
                is Response.Success -> insertList(it.data)
            }
        }
        return Result.success()
    }

    private fun insertList(stores: List<StoreFirebaseEntity>) {
        Log.d(TAG, "insertList...")
        scope.launch {
            storeRepo.insert(stores)
        }

    }

}