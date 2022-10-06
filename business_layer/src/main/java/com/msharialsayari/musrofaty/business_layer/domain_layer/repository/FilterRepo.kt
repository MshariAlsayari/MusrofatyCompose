package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.*
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterAdvancedModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toFilterAdvancedEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilterRepo @Inject constructor(
    private val dao: FilterAdvancedDao,
    private val filterDao: FilterDao,
    private val senderRepo: SenderRepo,
) {

    suspend fun getAll( senderId:Int): List<FilterAdvancedModel> {
        val finalList = mutableListOf<FilterAdvancedModel>()
        dao.getSenderFilters(senderId).map {entity ->
            finalList.add(entity.toFilterAdvancedModel())
        }
        return finalList
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


    suspend fun migrateForFilters() {
        val senders = senderRepo.getAllSenders()
        val filters = filterDao.getAll()
        val filterAdvanced = mutableListOf<FilterAdvancedEntity>()
        filters.forEach { filter->
           val sender =  senders.find { it.senderName.equals(filter.bankName , ignoreCase = true) }
            if (sender != null){
                val entity = filter.toFilterModel(sender.id).toFilterAdvancedEntity()
                val newWord = FilterAdvancedModel.getFilterWordsAsList(entity.words)[0]
                entity.words = newWord
                filterAdvanced.add(entity)
            }
        }
        dao.insertAll(*filterAdvanced.toTypedArray())
    }



}