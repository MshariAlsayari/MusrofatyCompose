package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import android.content.Context
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreAndCategoryModel
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.toStoreModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.StoreModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toStoreEntity
import com.msharialsayari.musrofaty.utils.SmsUtils
import com.msharialsayari.musrofaty.utils.enums.SmsType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoreRepo @Inject constructor(
    private val dao: StoreDao,
    private val categoryRepo: CategoryRepo,
    private val smsRepo: SmsRepo,
    @ApplicationContext val context: Context
) {

    private suspend fun getAllSms(): List<SmsModel> {
        return emptyList()
    }


    private suspend fun getExpensesSms(): List<SmsModel> {
        val smsList = getAllSms()
        return smsList.filter { it.smsType == SmsType.EXPENSES }
    }

    private suspend fun getAllStores(): List<StoreModel> {
        val returnedValue = mutableListOf<StoreModel>()
        val storesList = getExpensesSms().filter { SmsUtils.getStoreName(it.body).isNotEmpty() }
        storesList.forEach {
            val storeName = SmsUtils.getStoreName(it.body ?: "")
            var storeModel = getStoreByStoreName(storeName)

            if (storeModel == null) {
                storeModel = StoreModel(storeName = storeName, categoryId = -1)
            }

            if (returnedValue.find { it.storeName == storeName } == null) {
                returnedValue.add(storeModel)
            }
        }

        return returnedValue
    }



    suspend fun getStoreByStoreName(storeName: String): StoreModel? {
        val storeEntity = dao.getStoreByName(storeName)

        storeEntity?.let {
            return it.toStoreModel()
        } ?: run { return null }

    }

    suspend fun getStoreAndCategory(storeName: String) : StoreAndCategoryModel {
        var storeModel = getStoreByStoreName(storeName)

        if (storeModel == null){
            storeModel = StoreModel(storeName = storeName , categoryId = -1)
        }

        var categoryModel: CategoryModel? = categoryRepo.getCategory(storeModel.categoryId)

        if (categoryModel == null)
            categoryModel = CategoryModel(id = -1)

        return StoreAndCategoryModel(storeModel,categoryModel)
    }

    suspend fun getStoreAndCategoryBySmsList(smsList:List<SmsModel>) : List<SmsModel>{
        val finalList = mutableListOf<SmsModel>()
        smsList.forEach {
            it.storeAndCategoryModel = getStoreAndCategory(it.storeName)
            finalList.add(it)
        }
        return finalList

    }

    suspend fun getListOfStoreAndCategory():List<StoreAndCategoryModel>{
        val stores = getAllStores()
        val returnedValue = mutableListOf<StoreAndCategoryModel>()
        stores.map {
            returnedValue.add(getStoreAndCategory(it.storeName))
        }

        return returnedValue

    }


    suspend fun insertStore(storeModel: StoreModel) {
        dao.insert(storeModel.toStoreEntity())
    }

    suspend fun insert(list: List<StoreModel>) {
        val categoryList: MutableList<StoreEntity> = mutableListOf()
        list.forEach {
            categoryList.add(it.toStoreEntity())
        }

        dao.insert(*categoryList.toTypedArray())
    }

    suspend fun insertOrUpdateIfExisted(storeModel: StoreModel) {
        if (dao.getStoreByName(storeModel.storeName) == null) {
            val list = mutableListOf<StoreModel>()
            list.add(storeModel)
            insert(list)
        } else {
            dao.update(storeModel.toStoreEntity())
        }


    }



}