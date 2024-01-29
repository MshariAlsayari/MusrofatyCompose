package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.WordDetectorDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.WordDetectorEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.toWordDetectorModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.WordDetectorModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.WordDetectorType
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toWordDetectorEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordDetectorRepo @Inject constructor(
    private val dao: WordDetectorDao) {




    suspend fun getAll(type:WordDetectorType):List<WordDetectorModel> {
        val returnedList: MutableList<WordDetectorModel> = mutableListOf()
        dao.getAll(type.name).map {
            returnedList.add(it.toWordDetectorModel())
        }
        return returnedList
    }

     fun getAllFlowList(type:WordDetectorType):Flow<List<WordDetectorEntity>> {
        return dao.getAllFlowList(type.name)
    }

    suspend fun insert(vararg list: WordDetectorModel) {
        val words: MutableList<WordDetectorEntity> = mutableListOf()
        list.map {
            words.add(it.toWordDetectorEntity())
        }
        dao.insertAll(*words.toTypedArray())
    }

    suspend fun delete(id: Int) {
        dao.delete(id)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }




    suspend fun insert(list: List<WordDetectorModel>){
        insert(*list.toTypedArray())
    }


    suspend fun update( item: WordDetectorModel) {
        dao.update(item.toWordDetectorEntity())
    }
}