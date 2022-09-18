package com.msharialsayari.musrofaty.business_layer.data_layer.database.content_database

import androidx.room.*

@Dao
interface ContentDao {


    @Query("SELECT * FROM ContentEntity")
    suspend fun getAll(): List<ContentEntity>

    @Query("SELECT * FROM ContentEntity WHERE contentKey=:key")
    suspend fun getContentByKey(key:String): List<ContentEntity>

    @Query("SELECT * FROM ContentEntity WHERE id=:id")
    suspend fun getContentById(id:Int): ContentEntity?

    @Update
    suspend fun update(contentEntity: ContentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg contentEntity: ContentEntity)
}