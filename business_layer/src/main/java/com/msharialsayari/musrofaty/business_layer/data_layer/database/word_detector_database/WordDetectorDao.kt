package com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface WordDetectorDao {

    @Query("SELECT * FROM WordDetectorEntity WHERE type COLLATE NOCASE IN (:type)")
    suspend fun getAll(type:String): List<WordDetectorEntity>


    @Query("SELECT * FROM WordDetectorEntity WHERE type COLLATE NOCASE IN (:type)")
     fun getAllFlowList(type:String): Flow<List<WordDetectorEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg wordDetectorEntity: WordDetectorEntity)

    @Update
    suspend fun update(wordDetectorEntity: WordDetectorEntity)
}