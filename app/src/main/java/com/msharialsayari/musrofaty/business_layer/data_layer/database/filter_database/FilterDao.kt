package com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database


import androidx.room.Dao
import androidx.room.Query

@Dao
interface FilterDao {

    @Query("SELECT * FROM FilterEntity")
    suspend fun getAll(): List<FilterEntity>

}