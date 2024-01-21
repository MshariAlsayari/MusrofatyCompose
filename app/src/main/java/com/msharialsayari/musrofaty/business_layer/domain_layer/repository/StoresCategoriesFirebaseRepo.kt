package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import android.content.Context
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.msharialsayari.musrofaty.base.Response
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_firebase_database.StoreFirebaseDao
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.StoresCategoriesModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StoresCategoriesFirebaseRepo @Inject constructor(
    private val dao: StoreFirebaseDao,
    @ApplicationContext val context: Context
) {

    private val db          = Firebase.firestore
    private val collectionRef = db.collection(stores_categories_path)

    companion object{
        private const val stores_categories_path =  "storesCategories"
        private const val category_id_field =  "category_id"
        private const val keys_search_field =  "key_search"
    }

    suspend fun getStoresAndCategoriesFromFirebase() = flow {
        emit(Response.Loading())
        emit(Response.Success(collectionRef.get().await().documents.mapNotNull { doc ->
            val model = StoresCategoriesModel(categoryId =(doc.data?.get(category_id_field) as Long).toInt())
            val keysSearch = if(doc.data?.get(keys_search_field) is List<*> ) doc.data?.get(keys_search_field) as List<String> else emptyList()
            model.keysSearch =keysSearch
            return@mapNotNull model
        }))
    }. catch { error ->
        error.message?.let { errorMessage ->
            emit(Response.Failure(errorMessage))
        }
    }
}