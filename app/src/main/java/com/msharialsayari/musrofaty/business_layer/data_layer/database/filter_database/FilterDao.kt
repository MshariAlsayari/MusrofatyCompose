package com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow


@Dao
interface FilterDao {


    @Transaction
    @Query("SELECT * FROM FilterEntity")
    fun getAll(): Flow<List<FilterWithWordsEntity>>

    @Query("SELECT * FROM FilterEntity WHERE senderId =:senderId ")
    fun observingSenderFilters(senderId: Int): Flow<List<FilterEntity>>

    @Query("SELECT * FROM FilterEntity WHERE senderId =:senderId ")
    suspend fun getSenderFilters(senderId: Int): List<FilterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveFilters(vararg filters: FilterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWords(vararg filterWords: FilterWordEntity)

    @Query("DELETE FROM FilterEntity  WHERE id =:id ")
    suspend fun delete(id: Int)

    @Query("UPDATE  FilterEntity SET title =:title WHERE id =:id ")
    suspend fun updateFilterTitle(id: Int, title:String)
}