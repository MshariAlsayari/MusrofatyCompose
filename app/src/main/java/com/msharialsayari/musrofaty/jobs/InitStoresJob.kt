package com.msharialsayari.musrofaty.jobs

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.StoreModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.StoreFirebaseRepo
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.StoreRepo
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddOrUpdateStoreUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSmsModelListUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class InitStoresJob @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val getSmsListUseCase: GetSmsModelListUseCase,
    private val addOrUpdateStoreUseCase: AddOrUpdateStoreUseCase,
    private val storeRepo: StoreRepo,
    private val storeFirebaseRepo: StoreFirebaseRepo,
    ) : CoroutineWorker(appContext, workerParams) {

    private lateinit var smsList: List<SmsModel>

    companion object{
        private val TAG = InitStoresJob::class.java.simpleName
    }
    override suspend fun doWork(): Result {
       smsList =  getSmsListUseCase.invoke()
       Log.d(TAG, "doWork() smsList: ${smsList.size}")
       smsList.forEach {
            insertStore(it)
        }
        return Result.success()
    }



    private suspend fun insertStore(sms: SmsModel) {
        val smsStoreName = sms.storeName
        val store = storeRepo.getStoreByStoreName(smsStoreName)
        if (smsStoreName.isNotEmpty() && (store == null || store.categoryId == 0 || store.categoryId == -1)) {
            val storeFirebase = storeFirebaseRepo.getStoreByStoreName(smsStoreName)
            val model = if (storeFirebase != null) StoreModel(name = smsStoreName, categoryId = storeFirebase.category_id) else StoreModel(name = smsStoreName)
            addOrUpdateStoreUseCase.invoke(model)
        }
    }

}