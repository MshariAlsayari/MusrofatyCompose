package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import android.content.Context
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryWithStore
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.toCategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.SmsCategory
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toCategoryEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepo @Inject constructor(
    private val dao: CategoryDao,
    @ApplicationContext val context: Context
) {




    fun getAll():Flow<List<CategoryEntity>>{
        return dao.getAll()
    }


    suspend fun getCategory(categoryId: Int): CategoryModel? {
        return dao.getCategory(categoryId)?.toCategoryModel()
    }

     fun getCategoryAndStores(categoryId: Int): Flow<CategoryWithStore>? {
       return dao.getCategoryAndStores(categoryId)
    }


    suspend fun insert(list: List<CategoryModel>) {
        val categoryList: MutableList<CategoryEntity> = mutableListOf()
        list.map {
            categoryList.add(it.toCategoryEntity())
        }

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

    suspend fun insertDefaultCategoryList() {
        val categoryList: MutableList<CategoryEntity> = mutableListOf()
        SmsCategory.getAll().map {
            categoryList.add(it.toCategoryEntity())
        }
        dao.deleteAll()
        dao.insert(*categoryList.toTypedArray())

    }
}