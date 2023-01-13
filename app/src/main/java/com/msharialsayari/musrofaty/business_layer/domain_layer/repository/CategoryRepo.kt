package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.msharialsayari.musrofaty.base.Response
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.*
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toCategoryEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepo @Inject constructor(
    private val dao: CategoryDao,
    @ApplicationContext val context: Context
) {

    private val db              = Firebase.firestore
    private val queryCategories = db.collection(categories_path)

    companion object{
        private const val categories_path =  "categories"
    }




    fun getAll():Flow<List<CategoryEntity>>{
        return dao.getAll()
    }


    suspend fun getCategory(categoryId: Int): CategoryModel? {
        return dao.getCategory(categoryId)?.toCategoryModel()
    }

     fun getCategoryAndStores(categoryId: Int): Flow<CategoryWithStores>? {
       return dao.getCategoryAndStores(categoryId)
    }


    suspend fun insert(list: List<CategoryModel>) {
        val categoryList: MutableList<CategoryEntity> = mutableListOf()
        list.map {
            categoryList.add(it.toCategoryEntity())
        }

        Log.d("MshariTest", categoryList.size.toString())
        dao.insert(*categoryList.toTypedArray())
    }

    suspend fun insert(vararg model: CategoryModel) {
        val categoryList: MutableList<CategoryEntity> = mutableListOf()
        model.map {
            categoryList.add(it.toCategoryEntity())
        }

        dao.insert(*categoryList.toTypedArray())
    }

    suspend fun update(categoryModel: CategoryModel) {
        dao.update(categoryModel.toCategoryEntity())
    }

    suspend fun insertOrUpdateIfExisted(categoryModel: CategoryModel) {
        if (dao.getCategory(categoryModel.id) == null) {
            val list = mutableListOf<CategoryModel>()
            list.add(categoryModel)
            insert(list)
        } else {
            dao.update(categoryModel.toCategoryEntity())
        }


    }

    suspend fun delete(categoryModel: CategoryModel) {
        dao.delete(categoryModel.toCategoryEntity())
    }




     fun getCategoriesFromFirestore() = flow {
        emit(Response.Loading())
        emit(Response.Success(queryCategories.get().await().documents.mapNotNull { doc ->
            doc.toObject(CategoryEntity::class.java)
        }))
    }. catch { error ->
        error.message?.let { errorMessage ->
            emit(Response.Failure(errorMessage))
        }
    }



}