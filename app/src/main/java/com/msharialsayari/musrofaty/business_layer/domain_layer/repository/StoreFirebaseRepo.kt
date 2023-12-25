package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.msharialsayari.musrofaty.base.Response
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_firebase_database.StoreFirebaseDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_firebase_database.StoreFirebaseEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StoreFirebaseRepo @Inject constructor(
    private val dao: StoreFirebaseDao,
    @ApplicationContext val context: Context
)  {

    private val db          = Firebase.firestore
    private val queryStores = db.collection(stores_path)

    companion object{
        private const val stores_path =  "stores"
    }

    suspend fun getStoreByStoreName(storeName: String): StoreFirebaseEntity? {
        val storeEntity = dao.getStoreByName(storeName)
        storeEntity?.let {
            return it
        } ?: run { return null }

    }


    suspend fun insert(store: StoreFirebaseEntity) {
        dao.insert(store)
    }


    suspend fun insert(list: List<StoreFirebaseEntity>) {
        dao.insert(*list.toTypedArray())
    }

    fun getStoresFromFirestore() = flow {
        emit(Response.Loading())
        emit(Response.Success(queryStores.get().await().documents.mapNotNull { doc ->
            return@mapNotNull StoreFirebaseEntity(name = doc.data?.get("name") as String, category_id =(doc.data?.get("category_id") as Long).toInt())
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
            Log.d("StoreFirebaseRepo", "${storeEntity.name} addedd successfully")
        }.addOnFailureListener {
            Log.d("StoreFirebaseRepo", "${storeEntity.name} there was an error to add")
            Log.d("StoreFirebaseRepo", it.message?:"error")
        }
    }
}