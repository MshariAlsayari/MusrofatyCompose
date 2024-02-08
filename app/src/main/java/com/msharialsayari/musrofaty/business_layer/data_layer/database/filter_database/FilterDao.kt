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
    suspend fun getAll(): Flow<List<FilterWithWordsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveFilters(vararg filters: FilterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWords(vararg filterWords: FilterWordEntity)

    @Query("DELETE FROM FilterEntity  WHERE id =:id ")
    suspend fun delete(id: Int)

    @Query("UPDATE  FilterEntity SET title =:title WHERE id =:id ")
    suspend fun updateFilterTitle(id: Int, title:String)
}