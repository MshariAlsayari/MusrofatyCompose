package com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterAmountModel
import kotlinx.coroutines.flow.Flow


@Dao
interface FilterDao {


    @Transaction
    @Query("SELECT * FROM FilterEntity")
    fun getAll(): Flow<List<FilterWithWordsEntity>>

    @Transaction
    @Query("SELECT * FROM FilterEntity WHERE id =:id")
    suspend fun getFilter(id:Int): FilterWithWordsEntity?

    @Query("SELECT * FROM FilterEntity WHERE senderId =:senderId ")
    suspend fun getSenderFilters(senderId: Int): List<FilterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFilters(vararg filters: FilterEntity) : List<Long>

    @Query("DELETE FROM FilterEntity  WHERE id =:id ")
    suspend fun deleteFilter(id: Int)

    @Query("UPDATE  FilterEntity SET title =:title WHERE id =:id ")
    suspend fun updateFilterTitle(id: Int, title:String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilterWord(vararg filterWords: FilterWordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilterAmount(model: FilterAmountEntity)

    @Query("DELETE FROM FilterWordEntity  WHERE wordId =:id ")
    suspend fun deleteFilterWord(id: Int)

    @Query("DELETE FROM FilterAmountEntity  WHERE wordId =:id ")
    suspend fun deleteFilterAmount(id: Int)


}