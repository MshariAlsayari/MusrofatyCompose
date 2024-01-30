package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import android.content.Context
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.msharialsayari.musrofaty.business_layer.ApiResponse
import com.msharialsayari.musrofaty.business_layer.data_layer.categories.CategoryContainer
import com.msharialsayari.musrofaty.business_layer.data_layer.categories.CategoryDataSource
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryWithStores
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.toCategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toCategoryEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepo @Inject constructor(
    private val dao: CategoryDao,
    private val categoryDataSource: CategoryDataSource,
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

    fun getCategoriesList():List<CategoryEntity>{
        return dao.getCategoriesList()
    }

    fun getCategoriesWithStores(categoryId: Int?): Flow<List<CategoryWithStores>> {
        return if (categoryId != null)
            dao.getAllCategoriesWithStores(categoryId)
        else
            dao.getAllCategoriesWithStores()
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

        dao.insert(categoryList)
    }

    suspend fun insert(model: CategoryModel) {
        dao.insert(model.toCategoryEntity())
    }

    suspend fun update(categoryModel: CategoryModel) {
        dao.update(categoryModel.toCategoryEntity())
    }


    suspend fun delete(categoryModel: CategoryModel) {
        dao.delete(categoryModel.toCategoryEntity())
    }




//     fun getCategoriesFromFirestore() = flow {
//        emit(Response.Loading())
//        emit(Response.Success(queryCategories.get().await().documents.mapNotNull { doc ->
//            return@mapNotNull CategoryEntity(
//                id        = (doc.data?.get("id") as Long).toInt(),
//                valueAr   = doc.data?.get("valueAr") as String,
//                valueEn   = doc.data?.get("valueEn") as String,
//                isDefault = (doc.data?.get("isDefault") as Boolean),
//                sortOrder = (doc.data?.get("sortOrder") as Long).toInt() )
//        }))
//    }. catch { error ->
//        error.message?.let { errorMessage ->
//            emit(Response.Failure(errorMessage))
//        }
//    }

    suspend fun getCategoryFromBackend(): ApiResponse<CategoryContainer> {
        return categoryDataSource.getCategories()
    }





}