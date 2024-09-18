package com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface WordDetectorDao {

    @Query("SELECT * FROM WordDetectorEntity WHERE type COLLATE NOCASE IN (:type)")
    suspend fun getAll(type:String): List<WordDetectorEntity>


    @Query("SELECT * FROM WordDetectorEntity WHERE type COLLATE NOCASE IN (:type)")
     fun getAllFlowList(type:String): Flow<List<WordDetectorEntity>>

    @Query("SELECT * FROM WordDetectorEntity")
    fun getAll(): Flow<List<WordDetectorEntity>>

    @Query("DELETE  FROM WordDetectorEntity WHERE id =:id")
    suspend fun delete(id:Int)

    @Query("DELETE  FROM WordDetectorEntity WHERE type =:type")
    suspend fun deleteType(type: String)

    @Query("DELETE  FROM WordDetectorEntity")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg wordDetectorEntity: WordDetectorEntity)

    @Update
    suspend fun update(wordDetectorEntity: WordDetectorEntity)
}