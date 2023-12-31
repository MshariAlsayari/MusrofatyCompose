package com.msharialsayari.musrofaty.jobs

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.toSmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.StoreModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.StoreFirebaseRepo
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.StoreRepo
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSmsListUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class InitStoresJob @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val getSmsListUseCase: GetSmsListUseCase,
    private val storeRepo: StoreRepo,
    private val storeFirebaseRepo: StoreFirebaseRepo,
    ) : CoroutineWorker(appContext, workerParams) {

    private lateinit var smsList: List<SmsEntity>

    companion object{
        private val TAG = InitStoresJob::class.java.simpleName
    }
    override suspend fun doWork(): Result {
       smsList =  getSmsListUseCase.invoke()
       Log.d(TAG, "doWork() smsList: ${smsList.size}")
       smsList.forEach {
            insertStore(it.toSmsModel())
        }
        return Result.success()
    }



    private suspend fun insertStore(sms: SmsModel) {
        val smsStoreName = sms.storeName
        val store = storeRepo.getStoreByStoreName(smsStoreName)
        if (smsStoreName.isNotEmpty() && (store == null || store.categoryId == 0)) {
            val storeFirebase = storeFirebaseRepo.getStoreByStoreName(smsStoreName)
            val model = if (storeFirebase != null) StoreModel(name = sms.storeName, categoryId = storeFirebase.category_id) else StoreModel(name = sms.storeName)
            storeRepo.insertOrUpdateIfExisted(model)
        }
    }

}