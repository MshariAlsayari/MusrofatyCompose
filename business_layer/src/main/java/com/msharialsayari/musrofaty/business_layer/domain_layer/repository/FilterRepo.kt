package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.*
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterAdvancedModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toFilterAdvancedEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilterRepo @Inject constructor(
    private val dao: FilterAdvancedDao,
    private val filterDao: FilterDao
) {

    suspend fun getAll(senderName: String): List<FilterAdvancedModel> {
        val finalList = mutableListOf<FilterAdvancedModel>()
        val oldFilter = filterDao.getBankFilters(senderName)
        val advancedFilter = dao.getSenderFilters(senderName)
        oldFilter.map { entity ->
            finalList.add(entity.toFilterModel())
        }

        advancedFilter.map {entity ->
            finalList.add(entity.toFilterAdvancedModel())
        }
        return finalList
    }

    suspend fun getAll(): List<FilterAdvancedModel> {
        val finalList = mutableListOf<FilterAdvancedModel>()
        val oldFilter = filterDao.getAll()
        val advancedFilter = dao.getAll()
        oldFilter.map { entity ->
            finalList.add(entity.toFilterModel())
        }

        advancedFilter.map {entity ->
            finalList.add(entity.toFilterAdvancedModel())
        }
        return finalList
    }


    suspend fun insert(vararg list: FilterAdvancedModel) {
        val filters: MutableList<FilterAdvancedEntity> = mutableListOf()
        list.map {
            filters.add(it.toFilterAdvancedEntity())
        }

        dao.insertAll(*filters.toTypedArray())
    }

    suspend fun delete(list: List<FilterAdvancedModel>) {
        val finalList = mutableListOf<FilterAdvancedEntity>()
        list.forEach {
            filterDao.delete(it.id)
            finalList.add(it.toFilterAdvancedEntity())
        }
        dao.delete(*finalList.toTypedArray())
    }

}