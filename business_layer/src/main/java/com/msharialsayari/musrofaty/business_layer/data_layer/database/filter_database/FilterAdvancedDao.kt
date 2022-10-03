package com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database


import androidx.room.*

@Dao
interface FilterAdvancedDao {

    @Query("SELECT * FROM FilterAdvancedEntity")
    suspend fun getAll(): List<FilterAdvancedEntity>

    @Query("SELECT * FROM FilterAdvancedEntity WHERE senderId =:senderId ")
    suspend fun getSenderFilters(senderId: Int): List<FilterAdvancedEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg filterEntity: FilterAdvancedEntity)

    @Delete
    suspend fun delete(vararg filterEntity: FilterAdvancedEntity)
}