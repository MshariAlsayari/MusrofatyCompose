package com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database

import androidx.room.*

@Dao
interface SenderDao {


    @Query("SELECT * FROM SenderEntity")
    suspend fun getAll():List<SenderEntity>

    @Query("SELECT * FROM SenderEntity WHERE isActive = 1")
    suspend fun getAllActive():List<SenderEntity>


    @Update
    suspend fun update(senderEntity: SenderEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg senderEntity: SenderEntity)
}