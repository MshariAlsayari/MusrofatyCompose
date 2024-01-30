package com.msharialsayari.musrofaty.jobs

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.msharialsayari.musrofaty.business_layer.ApiResponse
import com.msharialsayari.musrofaty.business_layer.data_layer.categories.CategoryContainer
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.CategoryRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class InitCategoriesFirebaseJob @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val categoryRepo: CategoryRepo,

) : CoroutineWorker(appContext, workerParams) {


    companion object{
        private val TAG = InitCategoriesFirebaseJob::class.java.simpleName
    }
    override suspend fun doWork(): Result {
//        categoryRepo.getCategoriesFromFirestore().collect{
//            when (it) {
//                is Response.Failure -> Log.d(TAG, "Failure... " + it.errorMessage)
//                is Response.Loading -> Log.d(TAG, "Loading...")
//                is Response.Success -> insertList(it.data)
//            }
//        }

        val result = categoryRepo.getCategoryFromBackend()
        when (result) {
            is ApiResponse.Failure -> Log.d(TAG, "Failure... " + result.errorResponse.errorMessage)
            is ApiResponse.Success -> insertList(result.data)
        }


        return Result.success()
    }

    private suspend fun insertList(container: CategoryContainer?) {
        Log.d(TAG,  "insertList()..." + "categories:" + container?.categories?.size)
        container?.categories?.let {
            categoryRepo.insert(it)
        }
    }

}