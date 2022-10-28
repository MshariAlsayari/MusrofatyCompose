package com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FilterAdvancedDao {

    @Query("SELECT * FROM FilterAdvancedEntity")
    suspend fun getAll(): List<FilterAdvancedEntity>

    @Query("SELECT * FROM FilterAdvancedEntity WHERE senderId =:senderId ")
    suspend fun getSenderFilters(senderId: Int): List<FilterAdvancedEntity>

    @Query("SELECT * FROM FilterAdvancedEntity WHERE id =:id ")
     fun getFilter(id: Int): Flow<FilterAdvancedEntity>

    @Query("SELECT * FROM FilterAdvancedEntity WHERE id =:id ")
    suspend fun getFilterById(id: Int): FilterAdvancedEntity

    @Query("UPDATE FilterAdvancedEntity SET words =:word WHERE id =:id ")
    suspend fun addFilterWord(id: Int,word:String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg filterEntity: FilterAdvancedEntity)


    @Update
    suspend fun update(filterEntity: FilterAdvancedEntity)

    @Delete
    suspend fun delete(vararg filterEntity: FilterAdvancedEntity)

    @Query("DELETE FROM FilterAdvancedEntity  WHERE id =:id ")
    suspend fun delete(id: Int)
}