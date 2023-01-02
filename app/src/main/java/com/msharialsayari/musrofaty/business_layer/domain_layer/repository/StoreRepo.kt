package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import android.content.Context
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.*
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.StoreModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toStoreEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoreRepo @Inject constructor(
    private val dao: StoreDao,
    private val categoryRepo: CategoryRepo,
    @ApplicationContext val context: Context
) {

     fun getAll(storeName:String=""): Flow<List<StoreWithCategory>> {
        return dao.getAll(storeName)

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

        val categoryModel: CategoryModel? = categoryRepo.getCategory(storeModel.categoryId)

        return StoreAndCategoryModel(storeModel,categoryModel)
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

    suspend fun update(storeModel: StoreModel) {
       dao.update(storeModel.toStoreEntity())
    }



}