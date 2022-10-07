package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import android.content.Context
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryWithStoreModel
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.toCategoryModel
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.toStoreModel
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


    suspend fun getAllCategory(): List<CategoryWithStoreModel> {
        val categoryList = mutableListOf<CategoryWithStoreModel>()
        dao.getAllCategoriesWithStores().map {
            val model = CategoryWithStoreModel(
                category = it.category.toCategoryModel(),
                stores = it.stores.map { store -> store.toStoreModel() })
            categoryList.add(model)
        }
        return categoryList
    }

    fun getAll():Flow<List<CategoryEntity>>{
        return dao.getAll()
    }


    suspend fun getCategory(categoryId: Int): CategoryModel? {
        return dao.getCategory(categoryId)?.toCategoryModel()
    }

    suspend fun getCategoryAndStores(categoryId: Int): CategoryWithStoreModel? {
        val categoryWithStoresEntity = dao.getCategoryAndStores(categoryId)
        var categoryEntity: CategoryEntity? = null
        var storiesEntity: List<StoreEntity>? = null
        categoryEntity = categoryWithStoresEntity?.category ?: dao.getCategory(categoryId)
        storiesEntity = categoryWithStoresEntity?.stores

        return if (categoryEntity != null)
            CategoryWithStoreModel(
                category = categoryEntity.toCategoryModel(),
                stores = storiesEntity?.map { it.toStoreModel() } ?: emptyList())
        else
            null

    }


    suspend fun insert(list: List<CategoryModel>) {
        val categoryList: MutableList<CategoryEntity> = mutableListOf()
        list.map {
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

    suspend fun delete(vararg list: CategoryModel) {
        val categoryList: MutableList<CategoryEntity> = mutableListOf()
        list.forEach {
            categoryList.add(it.toCategoryEntity())
        }
        dao.delete(*categoryList.toTypedArray())
    }

    suspend fun resetDefaultCategories() {
        val categoryList: MutableList<CategoryEntity> = mutableListOf()
        SmsCategory.getAll().map {
            categoryList.add(it.toCategoryEntity())
        }
        dao.deleteAll()
        dao.insert(*categoryList.toTypedArray())

    }
}