package com.msharialsayari.musrofaty.jobs

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.msharialsayari.musrofaty.base.Response
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_firebase_database.StoreFirebaseEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.StoreFirebaseRepo
import com.msharialsayari.musrofaty.utils.Constants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class InitStoresFirebaseJob @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val storeFirebaseRepo: StoreFirebaseRepo,

    ) : CoroutineWorker(appContext, workerParams) {


    companion object{
        private val TAG  = InitStoresFirebaseJob::class.java.simpleName
    }
    override suspend fun doWork(): Result {

        Log.d(TAG, "doWork() running...")

        if (runAttemptCount > Constants.ATTEMPTS_COUNT) {
            Log.d(TAG, "doWork() Result.failure")
            return Result.failure()
        }

        try {
            storeFirebaseRepo.getStoresFromFirebase().collect{
                when (it) {
                    is Response.Failure -> Log.d(TAG, "Failure... " + it.errorMessage)
                    is Response.Loading -> Log.d(TAG, "Loading...")
                    is Response.Success -> insertList(it.data)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "doWork() Result.retry")
            return Result.retry()
        }


        Log.d(TAG, "doWork() Result.success")
        return Result.success()
    }

    private suspend fun insertList(stores: List<StoreFirebaseEntity>) {
        Log.d(TAG, "insertList() stores:${stores.size}")
        storeFirebaseRepo.insert(stores)
    }

}