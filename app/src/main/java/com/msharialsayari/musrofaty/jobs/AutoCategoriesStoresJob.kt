package com.msharialsayari.musrofaty.jobs

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.StoreModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.StoresCategoriesModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toStoreEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.repository.CategoryRepo
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddOrUpdateStoreUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetAllSmsContainsStoreUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetStoresCategoriesKeysUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.PostStoreToFirebaseUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class AutoCategoriesStoresJob @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val getAllSmsContainsStoreUseCase: GetAllSmsContainsStoreUseCase,
    private val categoryRepo:CategoryRepo,
    private val addOrUpdateStoreUseCase: AddOrUpdateStoreUseCase,
    private val postStoreToFirebaseUseCase: PostStoreToFirebaseUseCase,
    private val getStoresCategoriesKeysUseCase: GetStoresCategoriesKeysUseCase,
    ) : CoroutineWorker(appContext, workerParams) {

    companion object {
        private val TAG = AutoCategoriesStoresJob::class.java.simpleName
    }

    var categories = emptyList<CategoryEntity>()
    var list : List<StoresCategoriesModel> = emptyList()


    override suspend fun doWork(): Result {
//        categories = categoryRepo.getCategoriesList()
//        getStoresCategoriesKeysUseCase().collect {
//            when (it) {
//                is Response.Failure -> Log.d(TAG, "Failure... " + it.errorMessage)
//                is Response.Loading -> Log.d(TAG, "Loading...")
//                is Response.Success -> getList(it.data)
//            }
//
//        }
//        if(categories.isNotEmpty() && list.isNotEmpty()){
//            categorising()
//        }
        return Result.success()
    }

    private  fun getList(stores: List<StoresCategoriesModel>) {
        Log.d(TAG,  "getList()..." + "list of categories:" + stores.size)
         list = stores
    }

    private suspend fun categorising() {
        Log.d(TAG,  "categorising()..." + "list of categories:" + list.size)
        list.map {model->
            model.keysSearch.map {
                val smsList =  getAllSmsContainsStoreUseCase.invoke(storeName = it)
                Log.d(TAG , "categorising() search_key:$it ")
                Log.d(TAG , "categorising() smsList:${smsList.size} ")
                val filteredList = smsList.filter { sms -> sms.storeAndCategoryModel == null || sms.storeAndCategoryModel?.category == null }
                Log.d(TAG , "categorising() filteredList:${filteredList.size} ")
                filteredList.map { sms ->
                        val storeName = sms.storeName
                        val storeModel = StoreModel(name = storeName, categoryId = model.categoryId)
                        addOrUpdateStoreUseCase.invoke(storeModel)
                        postStoreToFirebaseUseCase.invoke(storeModel.toStoreEntity())
                    }
            }

        }

    }

}