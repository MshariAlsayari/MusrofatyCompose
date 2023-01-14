package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.msharialsayari.musrofaty.base.Response
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.*
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.StoreModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toStoreEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoreRepo @Inject constructor(
    private val dao: StoreDao,
    private val categoryRepo: CategoryRepo,
    @ApplicationContext val context: Context
) {

    private val db          = Firebase.firestore
    private val queryStores = db.collection(stores_path)

    companion object{
        private const val stores_path =  "stores"
    }

     fun getAll(storeName:String=""): Flow<List<StoreWithCategory>> {
        return dao.getAll(storeName)
    }

    fun getStoreByCategory(categoryId:Int): Flow<List<StoreEntity>> {
        return dao.getStoreByCategoryId(categoryId)
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
            storeModel = StoreModel(name = storeName , categoryId = -1)
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
        if (dao.getStoreByName(storeModel.name) == null) {
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


    fun getStoresFromFirestore() = flow {
        emit(Response.Loading())
        emit(Response.Success(queryStores.get().await().documents.mapNotNull { doc ->
            return@mapNotNull StoreEntity(name = doc.data?.get("name") as String, category_id =(doc.data?.get("category_id") as Long).toInt())
        }))
    }. catch { error ->
        error.message?.let { errorMessage ->
            emit(Response.Failure(errorMessage))
        }
    }

    fun postStoreToFirestare(storeEntity: StoreEntity)  {
        val data = hashMapOf(
            "name" to storeEntity.name,
            "category_id" to storeEntity.category_id
        )


        queryStores.document(storeEntity.name).set(data).addOnSuccessListener {
            Log.d("MshariTest", "${storeEntity.name} addedd successfully")
        }.addOnFailureListener {
            Log.d("MshariTest", "${storeEntity.name} there was an error to add")
        }
    }





}