package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.msharialsayari.musrofaty.business_layer.data_layer.database.content_database.ContentDao
import com.msharialsayari.musrofaty.business_layer.data_layer.database.content_database.toContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toContentEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentRepo @Inject constructor(
    private val dao: ContentDao
) {


    suspend fun getAll(): List<ContentModel>{
        return dao.getAll().map { it.toContentModel() }.toList()
    }

    suspend fun getContentByKey(key:String): List<ContentModel>{
        return dao.getContentByKey(key).map { it.toContentModel() }.toList()
    }

    suspend fun getContentById(id:Int): ContentModel?{
        return dao.getContentById(id)?.toContentModel()
    }


    suspend fun update(model: ContentModel){
        dao.update(model.toContentEntity())
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg model: ContentModel){
        val list = model.toList().map { it.toContentEntity() }.toList()
        dao.insert(*list.toTypedArray())
    }
}