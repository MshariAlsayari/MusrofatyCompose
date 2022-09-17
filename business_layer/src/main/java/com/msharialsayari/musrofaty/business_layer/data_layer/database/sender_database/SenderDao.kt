package com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database

import androidx.room.*
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryWithStore

@Dao
interface SenderDao {


    @Query("SELECT * FROM SenderEntity")
    suspend fun getAll():List<SenderEntity>

    @Transaction
    @Query("SELECT * FROM SenderEntity")
    suspend fun getAllSendersWithSms(): List<SenderWithRelations>

    @Query("SELECT * FROM SenderEntity WHERE isActive = 1")
    suspend fun getAllActive():List<SenderEntity>


    @Update
    suspend fun update(senderEntity: SenderEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg senderEntity: SenderEntity)
}