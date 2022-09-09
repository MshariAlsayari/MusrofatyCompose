package com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database


import androidx.room.*

@Dao
interface FilterDao {

    @Query("SELECT * FROM FilterEntity")
    suspend fun getAll(): List<FilterEntity>

    @Query("SELECT * FROM FilterEntity WHERE bankName COLLATE NOCASE == (:bankName)")
    suspend fun getBankFilters(bankName: String): List<FilterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg filterEntity: FilterEntity)

    @Delete
    suspend fun delete(vararg filterEntity: FilterEntity)
}