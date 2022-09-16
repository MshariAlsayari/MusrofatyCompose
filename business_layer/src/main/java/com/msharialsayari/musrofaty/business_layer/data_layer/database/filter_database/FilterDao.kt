package com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database


import androidx.room.*
import com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database.FilterEntity

@Dao
interface FilterDao {

    @Query("SELECT * FROM FilterEntity")
    suspend fun getAll(): List<FilterEntity>

    @Query("SELECT * FROM FilterEntity WHERE bankName COLLATE NOCASE == (:bankName)")
    suspend fun getBankFilters(bankName: String): List<FilterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg filterEntity: FilterEntity)

    @Query("Delete from FilterEntity WHERE id =:id")
    suspend fun delete(id: Int)
}