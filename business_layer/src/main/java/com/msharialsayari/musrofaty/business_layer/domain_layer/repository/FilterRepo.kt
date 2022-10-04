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
                filterAdvanced.add(entity)

            }
        }
        dao.insertAll(*filterAdvanced.toTypedArray())
    }



}