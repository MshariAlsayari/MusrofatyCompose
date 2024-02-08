package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.FilterAdvancedDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.FilterAdvancedEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.FilterDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.FilterEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.FilterWordEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.toFilterAdvancedModel
import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.toFilterModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterAdvancedModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterWordModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toFilterAdvancedEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toFilterEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toFilterWordEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilterRepo @Inject constructor(
    private val dao: FilterAdvancedDao,
    private val filterDao: FilterDao,
) {

    fun observingSenderFilters(senderId:Int): Flow<List<FilterModel>> {
        return filterDao.observingSenderFilters(senderId).map { list ->
            list.map {
                it.toFilterModel()
            }
        }
    }

    suspend fun getAll(senderId:Int): List<FilterModel> {
        return filterDao.getSenderFilters(senderId).map { entity ->
            entity.toFilterModel()
        }
    }

    suspend fun getAll(): List<FilterAdvancedModel> {
        val finalList = mutableListOf<FilterAdvancedModel>()
        dao.getAll().map {entity ->
            finalList.add(entity.toFilterAdvancedModel())
        }
        return finalList
    }

    suspend fun getFilter(id:Int): FilterAdvancedModel {
        return dao.getFilterById(id).toFilterAdvancedModel()

    }

    suspend fun addFilterWord(id:Int, word:String) {
         val entity =  dao.getFilterById(id)
         val list =   FilterAdvancedModel.getFilterWordsAsList(entity.words).toMutableList()
         list.add(word)
         dao.addFilterWord(id, FilterAdvancedModel.getFilterWordsAsString(list))
    }


    suspend fun insert(vararg list: FilterAdvancedModel) {
        val filters: MutableList<FilterAdvancedEntity> = mutableListOf()
        list.map {
            filters.add(it.toFilterAdvancedEntity())
        }

        dao.insertAll(*filters.toTypedArray())
    }

    suspend fun update(model: FilterAdvancedModel) {
        dao.update(model.toFilterAdvancedEntity())
    }

    suspend fun delete(id: Int) {
        dao.delete(id)
    }

    suspend fun delete(list: List<FilterAdvancedModel>) {
        val finalList = mutableListOf<FilterAdvancedEntity>()
        list.forEach {
            finalList.add(it.toFilterAdvancedEntity())
        }
        dao.delete(*finalList.toTypedArray())
    }


    fun saveFilter(vararg list: FilterModel) {
        val filters: MutableList<FilterEntity> = mutableListOf()
        list.map {
            filters.add(it.toFilterEntity())
        }

        filterDao.saveFilters(*filters.toTypedArray())
    }

    fun saveFilterWords(vararg list: FilterWordModel) {
        val filters: MutableList<FilterWordEntity> = mutableListOf()
        list.map {
            filters.add(it.toFilterWordEntity())
        }

        filterDao.saveWords(*filters.toTypedArray())
    }





}