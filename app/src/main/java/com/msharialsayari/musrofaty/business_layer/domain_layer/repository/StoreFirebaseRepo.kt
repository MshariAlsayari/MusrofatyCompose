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
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StoreFirebaseRepo @Inject constructor(
    private val dao: StoreFirebaseDao,
    @ApplicationContext val context: Context
)  {

    private val db          = Firebase.firestore
    private val collectionRef = db.collection(stores_path)
    private val collectionNewRef = db.collection(new_stores_path)


    companion object{
        private var TAG = StoreFirebaseRepo::class.java.simpleName
        private const val stores_path =  "stores"
        private const val new_stores_path =  "newStores"
        private const val category_id_field =  "category_id"
        private const val name_search_field =  "name"
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

    fun getStoresFromFirebase() = flow {
        emit(Response.Loading())
        emit(Response.Success(collectionNewRef.get().await().documents.mapNotNull { doc ->
            val name = doc[name_search_field].toString()
            val categoryId = (doc[category_id_field] as Long).toInt()
            return@mapNotNull StoreFirebaseEntity(name = name, category_id =categoryId)
        }))
    }. catch { error ->
        error.message?.let { errorMessage ->
            emit(Response.Failure(errorMessage))
        }
    }

    suspend fun submitStoreToFirebase(storeEntity: StoreEntity)  {
        val data = hashMapOf(
            name_search_field to storeEntity.name,
            category_id_field to storeEntity.category_id
        )


        val documents = collectionNewRef.whereEqualTo(name_search_field,storeEntity.name).get().await().documents
        val documentData = if(documents.isNotEmpty()) documents.first().data else null
        val documentId = if(documents.isNotEmpty()) documents.first().id else null
        val entity = if(documentData != null) {
            val name = documentData[name_search_field].toString()
            val categoryId = (documentData[category_id_field] as Long).toInt()
            StoreEntity(name = name, category_id= categoryId)
        } else null
        val needToUpdate = entity?.category_id != storeEntity.category_id


        if(needToUpdate){
            if (documentId == null){
                collectionNewRef.document().set(data).addOnCompleteListener {
                    Log.d(TAG, "submitStoreToFirebase() date: $data added successfully")
                }.addOnFailureListener {
                    Log.d(TAG, "submitStoreToFirebase() date: $data there was an error to add")
                    Log.d(TAG, "submitStoreToFirebase() " + (it.message ?: "error"))
                }
            }else{
                collectionNewRef.document(documentId).set(data).addOnSuccessListener {
                    Log.d(TAG, "documentId:$documentId date: $data added successfully")
                }.addOnFailureListener {
                    Log.d(TAG, "documentId:$documentId date: $data there was an error to add")
                    Log.d(TAG, "submitStoreToFirebase() " + (it.message?:"error"))
                }
            }

        }else{
            Log.d(TAG, "submitStoreToFirebase() date: $data no need to update")
        }


    }
}